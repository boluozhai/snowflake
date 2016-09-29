package com.boluozhai.snowflake.h2o.command.sf.repo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

import com.boluozhai.snowflake.cli.AbstractCLICommandHandler;
import com.boluozhai.snowflake.cli.CLIResponse;
import com.boluozhai.snowflake.cli.util.ParamReader;
import com.boluozhai.snowflake.cli.util.ParamReader.Builder;
import com.boluozhai.snowflake.cli.util.ParamSet;
import com.boluozhai.snowflake.context.SnowflakeContext;
import com.boluozhai.snowflake.core.SnowflakeException;
import com.boluozhai.snowflake.datatable.DataClient;
import com.boluozhai.snowflake.datatable.Transaction;
import com.boluozhai.snowflake.h2o.data.H2oDataTable;
import com.boluozhai.snowflake.h2o.data.dao.AliasDAO;
import com.boluozhai.snowflake.h2o.data.pojo.element.RepoItem;
import com.boluozhai.snowflake.h2o.data.pojo.model.RepoDTM;
import com.boluozhai.snowflake.util.IOTools;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.config.Config;
import com.boluozhai.snowflake.xgit.objects.GitObject;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.ObjectBank;
import com.boluozhai.snowflake.xgit.refs.Ref;
import com.boluozhai.snowflake.xgit.refs.RefManager;
import com.boluozhai.snowflake.xgit.repository.Repository;
import com.boluozhai.snowflake.xgit.site.RepositoryType;
import com.boluozhai.snowflake.xgit.site.SystemRepository;
import com.boluozhai.snowflake.xgit.site.XGitSite;
import com.boluozhai.snowflake.xgit.utils.RepositoryAgent;

public class CmdImport extends AbstractCLICommandHandler {

	@Override
	public void process(SnowflakeContext context, String command) {

		DataClient client = null;
		try {

			InnerTask task = new InnerTask(context);
			InnerRepoWrapper sys_repo = task.load_system_repo();
			InnerRepoWrapper cur_repo = task.load_current_repo();
			cur_repo.load_param();

			client = task.open_data_table();

			cur_repo.check_self();
			sys_repo.check_self();
			cur_repo.appendTo(sys_repo);

			CLIResponse cli = CLIResponse.Agent.getResponse(context);
			PrintStream out = cli.out();
			out.println("done.");

		} finally {
			IOTools.close(client);
		}

	}

	private interface InnerRepoWrapper {

		void appendTo(InnerRepoWrapper sys);

		void load_param();

		void check_self();

		String getType();

		ObjectId getRepoDescriptorId();

		GitObject getRepoDescriptorObject();

		ObjectId setRepoDescriptorId(ObjectId id);

	}

	final static String repo_desc_ref_name = "xgit.private_refs/repository/descriptor";

	private static abstract class InnerBaseRepoWrapper implements
			InnerRepoWrapper {

		protected final InnerTask task;
		protected final Repository repo;
		protected final Config config;
		protected final RefManager refs;
		protected final ObjectBank bank;

		public InnerBaseRepoWrapper(Repository repo, InnerTask task) {

			this.repo = repo;
			this.task = task;

			this.config = Config.Factory.getInstance(repo);
			this.refs = RefManager.Factory.getInstance(repo);
			this.bank = ObjectBank.Factory.getBank(repo);

		}

		public void check_text(String s1, String s2) {
			if (s1.equals(s2)) {
				return;
			} else {
				String msg = "the 2 strings not match: s1=[%s], s2=[%s]";
				msg = String.format(msg, s1, s2);
				throw new SnowflakeException(msg);
			}
		}

		@Override
		public String getType() {
			return config.getProperty(Config.xgit.siterepositorytype);
		}

		@Override
		public GitObject getRepoDescriptorObject() {
			ObjectId id = this.getRepoDescriptorId();
			return this.bank.object(id);
		}

		@Override
		public ObjectId getRepoDescriptorId() {

			String key = Config.xgit.siterepositorydescriptor;
			String refname = config.getProperty(key, null);
			if (refname == null) {
				return null;
			}
			Ref ref = this.refs.getReference(refname);
			if (!ref.exists()) {
				return null;
			}
			return ref.getId();

		}

		@Override
		public ObjectId setRepoDescriptorId(ObjectId id) {

			final String key = Config.xgit.siterepositorydescriptor;
			String refname = config.getProperty(key, null);
			if (refname == null) {
				refname = repo_desc_ref_name;
				config.setProperty(key, refname);
				try {
					config.save();
				} catch (IOException e) {
					throw new SnowflakeException(e);
				}
			}
			Ref ref = this.refs.getReference(refname);
			ref.setId(id);
			return id;
		}

		protected void importSystemRepoDescriptor(InnerRepoWrapper sys)
				throws IOException {

			GitObject sys_desc_obj = sys.getRepoDescriptorObject();
			ObjectId sys_desc_id = sys_desc_obj.id();
			GitObject my_sys_desc_obj = this.bank.object(sys_desc_id);

			InputStream in = null;
			OutputStream out = null;
			try {
				in = sys_desc_obj.entity().openZippedInput();
				out = my_sys_desc_obj.entity().openZippedOutput();

				IOTools.pump(in, out);

				out.close();
				out = null;

			} finally {
				IOTools.close(in);
				IOTools.close(out);
			}

		}

	}

	private static class InnerSysRepoWrapper extends InnerBaseRepoWrapper {

		public InnerSysRepoWrapper(Repository repo, InnerTask task) {
			super(repo, task);
		}

		@Override
		public void check_self() {

			this.check_text(RepositoryType.system, this.getType());

			ObjectId desc_id = this.getRepoDescriptorId();
			String desc_type = null;
			if (desc_id != null) {
				GitObject desc = this.bank.object(desc_id);
				desc_type = desc.type();
			}
			if (!GitObject.TYPE.descriptor.equals(desc_type)) {
				String msg = "the system repo don't have a available repo-descriptor.";
				throw new SnowflakeException(msg);
			}

		}

		@Override
		public void load_param() {
			throw new SnowflakeException("unsupported");
		}

		@Override
		public void appendTo(InnerRepoWrapper sys) {
			throw new SnowflakeException("unsupported");
		}

	}

	private static class InnerUserRepoWrapper extends InnerBaseRepoWrapper {

		private String op_user;
		private String op_repo;

		public InnerUserRepoWrapper(Repository repo, InnerTask task) {
			super(repo, task);
		}

		@Override
		public void check_self() {

			this.check_text(RepositoryType.user, this.getType());

			// check the user name , exists or not.
			AliasDAO alias_dao = new AliasDAO(this.task.data_client);
			String user = this.op_user;
			user = alias_dao.findUser(user);
			if (user == null) {
				String msg = "the user [%s] is not exists.";
				msg = String.format(msg, this.op_user);
				throw new SnowflakeException(msg);
			}

			// check the current repo's descriptor.
			ObjectId desc_id = this.getRepoDescriptorId();
			if (desc_id != null) {
				String msg = "the current repo have a repo-descriptor: %s";
				msg = String.format(msg, desc_id);
				throw new SnowflakeException(msg);
			}

		}

		@Override
		public void load_param() {

			Builder builder = ParamReader.newBuilder();
			builder.option("--user", "");
			builder.option("--repo", "");

			ParamReader reader = builder.create(this.task.context);
			ParamSet ps = ParamSet.create(reader);

			this.op_user = ps.getRequiredOption("--user");
			this.op_repo = ps.getRequiredOption("--repo");

		}

		@Override
		public void appendTo(InnerRepoWrapper sys) {

			try {
				ObjectId sys_desc_id = sys.getRepoDescriptorId();
				this.importSystemRepoDescriptor(sys);
				this.make_my_repo_descriptor(sys_desc_id);
				this.register_to_data_table();
			} catch (IOException e) {
				throw new SnowflakeException(e);
			}

		}

		private void register_to_data_table() {

			// TODO Auto-generated method stub

			final DataClient dc = this.task.data_client;
			final Transaction tx = dc.beginTransaction();

			final String repoid = this.op_repo;
			String uid = this.op_user;

			AliasDAO alias_dao = new AliasDAO(dc);
			uid = alias_dao.findUser(uid);

			RepoDTM repoinfo = dc.get(uid, RepoDTM.class);
			if (repoinfo == null) {
				repoinfo = new RepoDTM();
				dc.insert(uid, repoinfo);
			}

			Map<String, RepoItem> tab = repoinfo.getTable();
			if (tab.containsKey(repoid)) {
				String msg = "the repo with name [%s] exists.";
				msg = String.format(msg, repoid);
				throw new SnowflakeException(msg);
			}

			URI location = this.repo.getComponentContext().getURI();

			RepoItem item = new RepoItem();
			item.setName(repoid);
			item.setLocation(location.toString());
			item.setDescriptor(this.getRepoDescriptorId().toString());
			tab.put(repoid, item);

			dc.update(repoinfo);

			tx.commit();

		}

		private void make_my_repo_descriptor(ObjectId sys_desc_id)
				throws IOException {

			URI location = this.repo.getComponentContext().getURI();

			// make descriptor properties
			Properties props = new Properties();
			props.setProperty("timestamp",
					String.valueOf(System.currentTimeMillis()));
			props.setProperty("location", location.toString());
			props.setProperty("system", sys_desc_id.toString());

			// make object
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			props.store(baos, GitObject.TYPE.descriptor);

			byte[] ba = baos.toByteArray();
			GitObjectBuilder builder = bank.newBuilder(
					GitObject.TYPE.descriptor, ba.length);
			builder.write(ba, 0, ba.length);
			GitObject obj = builder.create();
			ObjectId id = obj.id();

			// update refs

			String refname = repo_desc_ref_name;
			Ref ref = this.refs.getReference(refname);
			ref.setId(id);

			// update config
			config.setProperty(Config.xgit.siterepositorydescriptor, refname);
			config.save();

		}
	}

	private static class InnerDataRepoWrapper extends InnerBaseRepoWrapper {

		public InnerDataRepoWrapper(Repository repo, InnerTask task) {
			super(repo, task);
		}

		@Override
		public void check_self() {

			this.check_text(RepositoryType.data, this.getType());

			ObjectId desc_id = this.getRepoDescriptorId();
			if (desc_id != null) {
				String msg = "the current repo have a repo-descriptor: %s";
				msg = String.format(msg, desc_id);
				throw new SnowflakeException(msg);
			}

		}

		@Override
		public void load_param() {
			// TODO Auto-generated method stub
			throw new SnowflakeException("unsupported");

		}

		@Override
		public void appendTo(InnerRepoWrapper sys) {
			throw new SnowflakeException("unsupported");
		}

	}

	private static class InnerTask {

		private final SnowflakeContext context;
		private DataClient data_client;

		public InnerTask(SnowflakeContext context) {
			this.context = context;
		}

		public InnerRepoWrapper load_current_repo() {

			RepositoryAgent agent = RepositoryAgent.Factory.get(context);
			Repository repo = agent.getRepository(context);

			Config conf = Config.Factory.getInstance(repo);
			String type = conf.getProperty(Config.xgit.siterepositorytype);

			if (type == null) {
				// error

			} else if (type.equals(RepositoryType.user)) {
				return new InnerUserRepoWrapper(repo, this);

			} else if (type.equals(RepositoryType.data)) {
				return new InnerDataRepoWrapper(repo, this);

			} else {
				// error
			}
			String msg = "unsupported repository type : " + type;
			throw new SnowflakeException(msg);
		}

		public InnerRepoWrapper load_system_repo() {
			XGitSite site = XGitSite.Agent.getSite(context);
			SystemRepository repo = site.getSystemRepository();
			InnerSysRepoWrapper wrapper = new InnerSysRepoWrapper(repo, this);
			return wrapper;
		}

		public DataClient open_data_table() {
			DataClient client = H2oDataTable.openClient(context);
			this.data_client = client;
			return client;
		}

	}

}
