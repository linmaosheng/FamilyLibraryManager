package familylibrarymanager.zhao.com.familylibrarymanager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import familylibrarymanager.zhao.com.familylibrarymanager.adapter.BookAdapter;
import familylibrarymanager.zhao.com.familylibrarymanager.bean.Book;
import familylibrarymanager.zhao.com.familylibrarymanager.constant.IntentConstant;
import familylibrarymanager.zhao.com.familylibrarymanager.constant.SQLConstant;
import familylibrarymanager.zhao.com.familylibrarymanager.dao.LibraryDBDao;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {
    //数据库操作变量
    private LibraryDBDao mDao;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout refreshLayout;
    private ListViewCompat list_books;
    private BookAdapter bookAdapter;
    private List<Book> data = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDao = new LibraryDBDao(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        data.clear();
        List<Book> list = mDao.queryAllBookList();
        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
        bookAdapter = new BookAdapter(getActivity(), data);
        list_books.setAdapter(bookAdapter);
    }

    private void initView(View view) {
        data.clear();
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        list_books = (ListViewCompat) view.findViewById(R.id.list_books);
        List<Book> list = mDao.queryAllBookList();
        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
        bookAdapter = new BookAdapter(getActivity(), data);
        list_books.setAdapter(bookAdapter);
        list_books.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                data.addAll(mDao.queryAllBookList());
                refreshLayout.setRefreshing(false);
                bookAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // 详情
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 图书
        String bookId = data.get(position).getId();
        String bookname = data.get(position).getBookname();
        String type = data.get(position).getType();
        String author = data.get(position).getAuthor();
        Double price =  data.get(position).getPrice();
        String borrower = data.get(position).getBorrower();
        String publicationDate = data.get(position).getPublicationDate();
        // 传参
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookId", bookId);
        bundle.putSerializable("bookName", bookname);
        bundle.putSerializable("type", type);
        bundle.putSerializable("author", author);
        bundle.putSerializable("price", price);
        bundle.putSerializable("borrower", borrower);
        bundle.putSerializable("publicationDate", publicationDate);
        intent.putExtras(bundle);
        intent.setClass(getActivity().getApplicationContext(),DetailsActivity.class);
        startActivity(intent);
    }
}
