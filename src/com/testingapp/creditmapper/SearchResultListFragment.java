package com.testingapp.creditmapper;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.testingapp.creditmapper.SearchResultDatabaseHelper.BusinessCursor;

public class SearchResultListFragment extends ListFragment implements LoaderCallbacks<Cursor>{

	private static final String TAG = "SearchResultListFragment";
	
	public static final String SEARCH_QUERY = "SEARCH_QUERY";
	private static final String FILENAME = "results.json";
	public static final String ARG_POSITION = "POSITION";

	private SearchResultJSONSerializer mSerializer;
	private ArrayList<BusinessResult> mResultList, mFavoriteList;
	private Callbacks mCallbacks;
	private BusinessResultListManager mManager;
	private static String mSearchString = null;

	public interface Callbacks {
		void onResultSelected(BusinessResult br);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		mSerializer = new SearchResultJSONSerializer(getActivity(), FILENAME);
		mFavoriteList = new ArrayList<BusinessResult>();
		mResultList = new ArrayList<BusinessResult>();
		mManager = BusinessResultListManager.getInstance(getActivity());
		mSearchString = getArguments().getString(SEARCH_QUERY);

		try {
			// Read results from a saved file
			mResultList = mSerializer.loadSearchResults();
		} catch (Exception e) {
		}

		// clean database before adding
		mManager.clearResults();

		// dummy results
		mManager.addResult(new BusinessResult());
		BusinessResult b = new BusinessResult();
		b.setMerchantName("Wendy's");
		b.getLocation().setCity("New York");
		mManager.addResult(b);
		
		getLoaderManager().initLoader(0, null, this);
	}

	private static class BusinessListCursorLoader extends SQLiteCursorLoader {
		public BusinessListCursorLoader(Context context) {
			super(context);
		}

		@Override
		protected Cursor loadCursor() {
			// Query the list of businesses
			Log.d(TAG, "Loaded Cursor: " + mSearchString);
			return BusinessResultListManager.getInstance(getContext())
					.getBusinessCursor(mSearchString);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return new BusinessListCursorLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		BusinessCursorAdapter adapter = new BusinessCursorAdapter(getActivity(), (BusinessCursor) cursor);
		setListAdapter(adapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		setListAdapter(null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);

		ListView listView = (ListView) v.findViewById(android.R.id.list);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

				@Override
				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					// TODO Auto-generated method stub
					switch (item.getItemId()) {

					case R.id.menu_item_favorite:
						BusinessCursorAdapter adapter = (BusinessCursorAdapter) getListAdapter();
						for (int i = 0; i < adapter.getCount(); i++) {
							if (getListView().isItemChecked(i)) {
								BusinessResult b = (BusinessResult) adapter.getItem(i);
								if (!b.isFavorite()) {
									mFavoriteList.add(b);
									b.setFavorite(true);
								}
							}
						}
						Log.i(SEARCH_QUERY, mFavoriteList.toString());
						mode.finish();
						return true;
					default:
						return false;
					}
				}

				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.search_result_list_context, menu);
					return true;
				}

				@Override
				public void onDestroyActionMode(ActionMode mode) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode mode,
						int position, long id, boolean checked) {
					// TODO Auto-generated method stub

				}

			});
		}

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_search_result, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			getActivity().onSearchRequested();
			return true;
		case R.id.menu_search_cancel:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(
				R.menu.search_result_list_context, menu);
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			mSerializer.saveSearchResults(mResultList);
			Log.i("SearchResultListFragment", "Save success!");
		} catch (Exception e) {
			Log.e("SearchResultListFragment", "Error saving results: " + e);
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		
		BusinessCursor cursor = (BusinessCursor) ((BusinessCursorAdapter)getListAdapter()).getItem(position);
		BusinessResult result = cursor.getBusiness(position);
		LocationDesc locResult = BusinessResultListManager.getInstance(getActivity())
				.getLocationCursor(result.getId()).getLocation();
		result.setLocation(locResult);

		// Add map fragment on click
		// mCallbacks.onResultSelected(result);

		Intent i = new Intent(getActivity(), SearchActivity.class);
		
		i.putExtra(SEARCH_QUERY, mSearchString);
		i.putExtra(ARG_POSITION, position);
		startActivity(i);
	}

	// CursorAdapter subclass
	private static class BusinessCursorAdapter extends CursorAdapter {
		private BusinessCursor mBusinessCursor;

		public BusinessCursorAdapter(Context context, BusinessCursor cursor) {
			super(context, cursor, 0);
			mBusinessCursor = cursor;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// Use a layout inflater to get a row view
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(R.layout.list_search_result,
					parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// Get the run for the current row
			BusinessResult result = mBusinessCursor.getBusiness(cursor.getPosition());
			LocationDesc locResult = BusinessResultListManager.getInstance(context).getLocationCursor(result.getId()).getLocation();
			result.setLocation(locResult);
			
			// Set up the start date text view
			TextView merchantInfo = (TextView) view
					.findViewById(R.id.list_search_result_merchantInfo);
			TextView location = (TextView) view
					.findViewById(R.id.list_search_result_location);
			TextView distance = (TextView) view
					.findViewById(R.id.list_search_result_distance);

			merchantInfo.setText(result.getMerchantName()
					+ " - "
					+ result.getMerchantCode()
					+ " "
					);
			location.setText(result.getLocation().toString());
			distance.setText(result.getDistance() + "mi");
		}
	}

	public static SearchResultListFragment newInstance(String searchString) {
		Bundle args = new Bundle();
		args.putString(SEARCH_QUERY, searchString);

		SearchResultListFragment fragment = new SearchResultListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	

}
