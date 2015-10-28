package com.example.fragment;

import com.example.get.DownloadManager;
import com.example.service.DownloadManagerService;


public class AllMissionsFragment extends MissionsFragment {

	@Override
	protected DownloadManager setupDownloadManager(
			DownloadManagerService.DMBinder binder) {
		return binder.getDownloadManager();
	}
}
