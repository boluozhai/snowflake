package com.boluozhai.snowflake.xgit.vfs.impl.objectbank;

import com.boluozhai.snowflake.mvc.model.ComponentContext;
import com.boluozhai.snowflake.mvc.model.ComponentLifecycle;
import com.boluozhai.snowflake.vfs.VFile;
import com.boluozhai.snowflake.xgit.ObjectId;
import com.boluozhai.snowflake.xgit.XGitContext;
import com.boluozhai.snowflake.xgit.objects.GitObjectBuilder;
import com.boluozhai.snowflake.xgit.objects.HashAlgorithmProvider;
import com.boluozhai.snowflake.xgit.vfs.FileObject;
import com.boluozhai.snowflake.xgit.vfs.FileObjectBank;
import com.boluozhai.snowflake.xgit.vfs.HashPathMapper;
import com.boluozhai.snowflake.xgit.vfs.base.FileXGitComponent;

public class FileObjectBankImpl implements FileXGitComponent, FileObjectBank {

	private final ComponentContext _context;
	private final VFile _file;
	private final FileObjectBankCore _core;
	public HashAlgorithmProvider _hash_alg;
	public HashPathMapper _hash_path_mapper;

	protected FileObjectBankImpl(ComponentContext context, VFile file) {

		FileObjectBankCore core = new FileObjectBankCore(context);

		this._context = context;
		this._file = file;
		this._core = core;
	}

	@Override
	public VFile getFile() {
		return this._file;
	}

	@Override
	public ComponentLifecycle lifecycle() {
		return new MyLife();
	}

	@Override
	public ComponentContext getComponentContext() {
		return this._context;
	}

	private class MyLife implements ComponentLifecycle {

		public MyLife() {
		}

		@Override
		public void onCreate() {

			FileObjectBankImpl self = FileObjectBankImpl.this;
			ComponentContext context = self._context;

			HashPathMapper hpm = context.getBean(
					XGitContext.component.hash_path_mapper,
					HashPathMapper.class);

			HashAlgorithmProvider halg = context.getBean(
					XGitContext.component.hash_algorithm,
					HashAlgorithmProvider.class);

			self._hash_alg = halg;
			self._hash_path_mapper = hpm;

		}
	}

	@Override
	public FileObject object(ObjectId id) {

		VFile base = this._file;
		VFile file = this._hash_path_mapper.getHashPath(base, id);

		FileObjectImpl.Builder builder = new FileObjectImpl.Builder();
		builder.id = id;
		builder.file = file;
		builder.bank = this;
		builder.core = this._core;

		return builder.create();
	}

	@Override
	public GitObjectBuilder newBuilder(String type, long length) {
		return new FileObjectBuilderDefault(_core, type, length);
	}

}
