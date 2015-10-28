package com.example.fragment;

import com.example.get.DownloadManager;
import com.example.get.FilteredDownloadManagerWrapper;
import com.example.service.DownloadManagerService;

public class DownloadingMissionsFragment extends MissionsFragment {
	@Override
	protected DownloadManager setupDownloadManager(
			DownloadManagerService.DMBinder binder) {
		return new FilteredDownloadManagerWrapper(binder.getDownloadManager(),
				false);
	}
}
