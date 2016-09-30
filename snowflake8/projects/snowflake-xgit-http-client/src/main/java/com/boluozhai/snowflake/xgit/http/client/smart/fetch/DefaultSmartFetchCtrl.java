package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.http.client.smart.DefaultSmartClientFactory;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientBuilder;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientFactory;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPktWrapper;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartTx;
import com.boluozhai.snowflake.xgit.repository.Repository;

public class DefaultSmartFetchCtrl implements SmartFetchController {

	interface define {

		String resource = "info/refs";
		String service = "xgit-upload-objects";

	}

	private final SmartFetchModel model;
	private final Map<ObjectId, SmartFetchItem> item_all;
	private final Map<ObjectId, SmartFetchItem> item_not_exists;
	private final ObjectId id_end;

	public DefaultSmartFetchCtrl(SmartFetchModel model) {

		this.model = model;
		model.setController(this);

		this.item_all = model.getAll();
		this.item_not_exists = model.getExistsNot();
		this.id_end = model.getEndId();

	}

	@Override
	public void add(ObjectId id) {
		if (id.equals(this.id_end)) {
			return;
		}
		SmartFetchItem item = this.item_all.get(id);
		if (item == null) {
			item = new SmartFetchItem(this.model, id);
			this.item_all.put(id, item);
			item.getController().check();
		}
	}

	@Override
	public void load() throws IOException {

		InnerLoader loader = new InnerLoader();
		for (; loader.has_more();) {
			loader.load();
		}

	}

	private class InnerLoader {

		private SmartClient smart_client;

		public InnerLoader() {

		}

		public void load() throws IOException {

			final Map<ObjectId, SmartFetchItem> table = DefaultSmartFetchCtrl.this.item_not_exists;
			final List<SmartFetchItem> list = new ArrayList<SmartFetchItem>(
					table.values());
			table.clear();

			// load

			this.load(list);

			// check after load

			for (SmartFetchItem item : list) {
				SmartFetchItemController ic = item.getController();
				ic.check();
				if (!ic.exists()) {
					String msg = "cannot fetch object: %s";
					msg = String.format(msg, item.getId());
					throw new SnowflakeException(msg);
				}
			}

		}

		private void load(List<SmartFetchItem> list) throws IOException {
			SmartTx tx = null;
			SmartRx rx = null;
			try {
				SmartClient client = this.get_smart_client();
				tx = client.openTx();

				for (SmartFetchItem it : list) {
					this.query_item(it, tx);
				}

				rx = tx.openRx();
				for (;;) {
					SmartPkt pkt = rx.read();
					if (pkt == null) {
						break;
					}
				}
			} finally {
				IOTools.close(tx);
				IOTools.close(rx);
			}
		}

		private void query_item(SmartFetchItem it, SmartTx tx)
				throws IOException {

			// TODO Auto-generated method stub

			ObjectId id = it.getId();

			SmartPkt pkt = new SmartPkt();
			SmartPktWrapper w = new SmartPktWrapper(pkt);

			pkt.setCommand(SmartPkt.COMMAND.want);
			pkt.getParam().add(id.toString());

			tx.write(pkt);

		}

		private SmartClient get_smart_client() {
			SmartClient client = this.smart_client;
			if (client == null) {
				SmartClientFactory factory = new DefaultSmartClientFactory();
				SmartClientBuilder builder = factory.newBuilder();

				ComponentContext cc = model.getBank().getComponentContext();
				Repository repo = (Repository) cc
						.getAttribute(XGitContext.component.repository);

				builder.setRemoteRepository(model.getRemote());
				builder.setLocalRepository(repo);
				builder.setDefaultResource(define.resource);
				builder.setDefaultService(define.service);

				client = builder.create();
				this.smart_client = client;
			}
			return client;
		}

		public boolean has_more() {
			return (!DefaultSmartFetchCtrl.this.item_not_exists.isEmpty());
		}
	}

}
