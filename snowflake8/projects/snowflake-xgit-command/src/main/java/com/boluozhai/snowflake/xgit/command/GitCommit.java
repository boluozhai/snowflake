package com.boluozhai.snowflake.xgit.command;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.vfs.VFS;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGit;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.dao.CommitDAO;
import com.boluozhai.snowflake.xgit.dao.TreeDAO;
import com.boluozhai.snowflake.xgit.meta.UriMeta;
import com.boluozhai.snowflake.xgit.meta.UriMetaManager;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.pojo.CommitObject;
import com.boluozhai.snowflake.xgit.pojo.CommitSectionObject;
import com.boluozhai.snowflake.xgit.pojo.Operator;
import com.boluozhai.snowflake.xgit.pojo.PlainId;
import com.boluozhai.snowflake.xgit.pojo.TreeItem;
import com.boluozhai.snowflake.xgit.pojo.TreeObject;
import com.boluozhai.snowflake.xgit.refs.Reference;
import com.boluozhai.snowflake.xgit.refs.ReferenceManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.repository.RepositoryManager;
import com.boluozhai.snowflake.xgit.utils.CurrentLocation;
import com.boluozhai.snowflake.xgit.vfs.FileWorkspace;

public class GitCommit extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		try {

			Task task = new Task(context);
			task.init();
			task.initParam();
			task.loadRef();
			task.loadParentCommit();
			task.makeTree();
			task.makeCommit();
			task.saveRef();
			task.printResult();

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// NOP
		}

	}

	private static class Param {

		private boolean section;
		private URI workspaceUri;
		private URI sectionUri;
		private String sectionPath;

		public String calculateSectionPath() {

			String s1 = this.workspaceUri.toString();
			String s2 = this.sectionUri.toString();

			if (s2.startsWith(s1)) {
				return s2.substring(s1.length());
			} else {
				return null;
			}

		}

	}

	private static class Task {

		private final SnowflakeContext _context; // the thread context

		private FileWorkspace _works;
		private UriMetaManager _uri_meta_man;
		private ObjectBank _obj_bank;

		private Param _param;

		private Reference _ref;
		private ObjectId _parent_commit_id;
		private ObjectId _parent_tree_id;
		private ObjectId _result_commit_id;
		private ObjectId _result_tree_id;

		public Task(SnowflakeContext context) {
			this._context = context;
		}

		public void init() {

			CurrentLocation cl = CurrentLocation.Factory.get(_context);
			URI loc = cl.getLocation(_context);
			RepositoryManager rm = XGit.getRepositoryManager(_context);
			Repository repo = rm.open(_context, loc, null);
			ComponentContext cc = repo.getComponentContext();

			FileWorkspace works = cc.getBean(XGitContext.component.workspace,
					FileWorkspace.class);
			UriMetaManager uri_meta_man = cc.getBean(
					XGitContext.component.uri_meta, UriMetaManager.class);
			ObjectBank bank = cc.getBean(XGitContext.component.objects,
					ObjectBank.class);

			Param param = new Param();
			param.sectionUri = loc;
			param.workspaceUri = works.getFile().toURI();

			this._works = works;
			this._uri_meta_man = uri_meta_man;
			this._obj_bank = bank;

			this._param = param;
		}

		public void initParam() {
			String section = _context.getParameter("--section", null);
			Param param = this._param;
			param.section = (section != null);
			param.sectionPath = param.calculateSectionPath();
		}

		public void loadRef() {
			String name = _param.section ? ReferenceManager.name.SECTION_HEAD
					: ReferenceManager.name.HEAD;
			ComponentContext cc = this._works.getComponentContext();
			ReferenceManager rm = cc.getBean(XGitContext.component.refs,
					ReferenceManager.class);
			Reference ref = rm.findTargetReference(name);
			this._ref = ref;
			System.out.println(ref.getName());
		}

		public void loadParentCommit() throws IOException {
			ObjectId parent_id = this._ref.getTargetId();
			if (parent_id == null) {
				return;
			}
			CommitDAO dao = CommitDAO.Factory.create(_obj_bank);
			CommitObject parent = dao.getCommit(parent_id);
			this._parent_commit_id = parent_id;
			this._parent_tree_id = parent.getTree();
		}

		public void makeTree() throws IOException {

			VFile file = _works.getFile();
			VFS vfs = file.vfs();
			file = vfs.newFile(_param.sectionUri);
			TreeMaker maker = new TreeMaker(this, file);

			final GitObject obj = maker.make();
			final String type = obj.type();
			if (!type.equals(GitObject.TYPE.tree)) {
				String msg = "bad git-object type:" + type;
				throw new RuntimeException(msg);
			}
			this._result_tree_id = obj.id();
		}

		public void makeCommit() throws IOException {

			final String msg = "todo ...";
			final long now = System.currentTimeMillis();
			final int timezone = TimeZone.getDefault().getRawOffset();

			ObjectId parent_commit = this._parent_commit_id;
			ObjectId parent_tree = this._parent_tree_id;
			ObjectId this_tree = this._result_tree_id;

			String section_path = this._param.sectionPath;

			if (this_tree.equals(parent_tree)) {
				throw new RuntimeException("nothing changed.");
			}

			// make operator

			Operator op = new Operator();
			op.setName("commit_maker");
			op.setTime(now);
			op.setZone(timezone);

			// make commit

			CommitObject commit = null;
			CommitSectionObject commit_section = null;

			if (_param.section) {
				commit_section = new CommitSectionObject();
				commit = commit_section;
			} else {
				commit = new CommitObject();
			}

			commit.setBody(msg);
			commit.setTree(this_tree);
			commit.setCommitter(op);
			commit.setAuthor(op);
			commit.setTime(now);
			if (parent_commit != null) {
				commit.addParent(parent_commit);
			}
			if (commit_section != null) {
				commit_section.setSection(section_path);
			}

			// save

			ObjectBank bank = this._obj_bank;
			CommitDAO dao = CommitDAO.Factory.create(bank);
			ObjectId commit_id = dao.saveCommit(commit);
			this._result_commit_id = commit_id;

		}

		public void saveRef() {

			ObjectId commit_id = this._result_commit_id;
			this._ref.setTargetId(commit_id);

		}

		public void printResult() {
			String msg = "commit:%s  tree:%s";
			msg = String.format(msg, this._result_commit_id,
					this._result_tree_id);
			System.out.println(msg);
		}

	}

	private static class CommitMaker {

	}

	private static class TreeMaker {

		private final VFile _file;
		private final Task _task;

		public TreeMaker(Task task, VFile file) {
			this._task = task;
			this._file = file;
		}

		public GitObject make() throws IOException {

			// load meta

			Class<TreeObject> type = TreeObject.class;
			URI uri = _file.toURI();
			UriMeta meta = _task._uri_meta_man.getMeta(type, uri);
			TreeObject tree = meta.loadJSON(type);

			// make sub tree(s)

			Map<String, TreeItem> items = tree.getItems();
			Set<String> keyset = items.keySet();
			String[] keys = keyset.toArray(new String[keyset.size()]);
			Arrays.sort(keys);

			for (String key : keys) {
				TreeItem item = items.get(key);
				if (item.getMode() == TreeItem.MODE.directory) {
					// a dir
					String name = item.getName();
					TreeMaker ch = this.child(name);
					GitObject obj = ch.make();
					item.setId(PlainId.convert(obj.id()));
				}
			}

			// save as a tree object
			ObjectBank bank = _task._obj_bank;
			TreeDAO dao = TreeDAO.Factory.create(bank);
			ObjectId id = dao.save(tree);

			return bank.object(id);

		}

		public TreeMaker child(String name) {
			VFile file = _file.child(name);
			return new TreeMaker(_task, file);
		}

	}

}
