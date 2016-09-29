package com.boluozhai.snowflake.xgit.command;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.http.client.GitHttpClient;
import com.boluozhai.snowflake.xgit.http.client.GitHttpRepo;
import com.boluozhai.snowflake.xgit.http.client.smart.DefaultSmartClientFactory;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClient;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartClientFactory;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartPkt;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartRx;
import com.boluozhai.snowflake.xgit.http.client.smart.SmartTx;
import com.boluozhai.snowflake.xgit.remotes.Remote;
import com.boluozhai.snowflake.xgit.remotes.RemoteManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class XGitLLFetch extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		try {

			CLIResponse cli = CLIResponse.Agent.getResponse(context);
			PrintStream out = cli.out();

			InnerTask task = new InnerTask(context);
			task.load_param();
			task.load_local_repo();
			task.load_remote_agent();
			task.load_smart_client();

			task.fetch_init();
			task.fetch_commit_objects();
			task.fetch_tree_objects();
			task.fetch_blob_objects();

			// a object

			// a blob

			// a set of blobs

			// a set of trees

			// a set of commits

			out.println("done.");

		} catch (IOException e) {

			throw new SnowflakeException(e);

		} finally {
		}

	}

	private static class InnerTask {

		private final SnowflakeContext context;

		private ObjectId param_want;
		private ObjectId param_have;
		private String param_remote;

		private Repository local_repo;
		private GitHttpRepo remote_repo;
		private SmartClient smart_client;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
		}

		public void load_smart_client() {

			SmartClientFactory factory = new DefaultSmartClientFactory();
			factory.setLocalRepository(this.local_repo);
			factory.setRemoteRepository(this.remote_repo);
			factory.setDefaultResource("info/refs");
			factory.setDefaultService("xgit-upload-objects");
			this.smart_client = factory.create();

		}

		public void fetch_init() throws IOException {
			SmartTx tx = null;
			SmartRx rx = null;
			try {

				tx = this.smart_client.openTx();

				SmartPkt pkt = new SmartPkt();
				pkt.setCommand(SmartPkt.COMMAND.want);
				pkt.setId(this.param_want);
				pkt.setContainEntity(true);

				tx.write(pkt);

				rx = tx.openRx();

				for (;;) {
					pkt = rx.read();
					if (pkt == null) {
						break;
					} else {
						// do something with pkt
					}
				}

			} finally {
				IOTools.close(rx);
				IOTools.close(tx);
			}
		}

		public void fetch_commit_objects() {

		}

		public void fetch_tree_objects() {

			ObjectId want = this.param_want;
			ObjectId have = this.param_have;

			SmallObjectsLoader loader = new SmallObjectsLoader(this);
			loader.setWant(want);
			loader.setHave(have);
			for (; loader.hasMore();) {
				loader.load_some();
			}

		}

		public void fetch_blob_objects() {
			// TODO Auto-generated method stub

		}

		public void load_local_repo() {
			RepositoryAgent agent = RepositoryAgent.Factory.get(context);
			Repository repo = agent.getRepository(context);
			this.local_repo = repo;
		}

		public void load_remote_agent() {

			RemoteManager rm = RemoteManager.Factory.getInstance(local_repo);
			Remote remote = rm.get(this.param_remote);

			GitHttpClient client = GitHttpClient.Factory.getInstance(context);
			GitHttpRepo repo = client.connect(URI.create(remote.getUrl()));

			// GitHttpResource res = repo.getResource("info/refs");
			// GitHttpService service =
			// res.getService("xgit-ll-upload-objects");

			this.remote_repo = repo;

		}

		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--remote", "");
			builder.option("--want", "");
			builder.option("--have", "");

			ParamReader reader = builder.create(context);
			ParamSet ps = ParamSet.create(reader);

			// if the begin is a commit, the end is needed.
			String remote = ps.getRequiredOption("--remote");
			String want = ps.getRequiredOption("--want");
			String have = ps.getOption("--have");

			if (have != null) {
				this.param_have = ObjectId.Factory.create(have);
			}

			if (want != null) {
				this.param_want = ObjectId.Factory.create(want);
			}

			this.param_remote = remote;

		}
	}

	private static class ObjectWrapper {

	}

	private static class SmallObjectsLoader {

		private Map<ObjectId, ObjectWrapper> current_pool;
		private Map<ObjectId, ObjectWrapper> next_pool;

		public SmallObjectsLoader(InnerTask task) {
			// TODO Auto-generated constructor stub

			this.current_pool = new HashMap<ObjectId, ObjectWrapper>();
			this.next_pool = new HashMap<ObjectId, ObjectWrapper>();

		}

		public boolean hasMore() {
			int s1 = this.current_pool.size();
			int s2 = this.next_pool.size();
			return ((s1 + s2) > 0);
		}

		public void setHave(ObjectId have) {
			// TODO Auto-generated method stub

		}

		public void setWant(ObjectId want) {
			// TODO Auto-generated method stub

		}

		public void load_some() {
			// TODO Auto-generated method stub

		}
	}

}
