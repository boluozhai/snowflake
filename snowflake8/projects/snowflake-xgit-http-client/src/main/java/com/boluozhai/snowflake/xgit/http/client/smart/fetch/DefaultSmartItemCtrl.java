package com.boluozhai.snowflake.xgit.http.client.smart.fetch;

import java.io.IOException;
import java.util.Map;

import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.dao.TreeDAO;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;

public class DefaultSmartItemCtrl implements SmartFetchItemController {

	private final SmartFetchItem item;
	private final GitObject object;
	private final ObjectId id;
	private final ObjectBank bank;

	private boolean loaded = false;

	public DefaultSmartItemCtrl(SmartFetchItem it) {

		this.item = it;
		it.setController(this);

		ObjectId id = it.getId();
		ObjectBank bank = it.getModel().getBank();
		GitObject obj = bank.object(id);
		this.object = obj;
		this.id = id;
		this.bank = bank;

	}

	public SmartFetchItem getItem() {
		return item;
	}

	@Override
	public void check() {

		if (object.exists()) {

			String type = object.type();
			if (type == null) {
				// NOP
			} else if (type.equals(GitObject.TYPE.commit)) {
				this.check_as_commit();
			} else if (type.equals(GitObject.TYPE.tree)) {
				this.check_as_tree();
			} else {
				// NOP
			}

		} else {
			Map<ObjectId, SmartFetchItem> tab = this.item.getModel()
					.getExistsNot();
			tab.put(id, this.item);
		}

	}

	private void check_as_tree() {
		try {
			SmartFetchController model_ctrl = this.item.getModel()
					.getController();
			TreeDAO dao = TreeDAO.Factory.create(bank);
			TreeObject tree = dao.getTree(id);
			Map<String, TreeItem> items = tree.getItems();
			for (TreeItem it : items.values()) {
				ObjectId id2 = PlainId.convert(it.getId());
				model_ctrl.add(id2);
			}
		} catch (IOException e) {
			throw new SnowflakeException(e);
		} finally {
			// NOP
		}
	}

	private void check_as_commit() {
		try {
			SmartFetchController model_ctrl = this.item.getModel()
					.getController();
			CommitDAO dao = CommitDAO.Factory.create(bank);
			CommitObject commit = dao.getCommit(id);
			ObjectId[] parents = commit.getParents();
			ObjectId tree = commit.getTree();
			model_ctrl.add(tree);
			if (parents != null) {
				for (ObjectId id2 : parents) {
					model_ctrl.add(id2);
				}
			}
		} catch (IOException e) {
			throw new SnowflakeException(e);
		} finally {
			// NOP
		}
	}

	@Override
	public boolean exists() {
		return object.exists();
	}

	@Override
	public boolean loaded() {
		return this.loaded;
	}

	@Override
	public void setLoaded() {
		this.loaded = true;
	}

}
