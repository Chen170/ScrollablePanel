//package com.ldcdev.library.固定列位置写死;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ldcdev.library.PanelAdapter;
//import com.ldcdev.library.R;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
///**
// * A flexible view for providing a limited window into a large data set,like a two-dimensional recyclerView.
// * but it will pin the itemView of first row and first column in their original location.
// */
//@SuppressLint("NotifyDataSetChanged")
//public class ScrollablePanel extends FrameLayout {
//
//    private OnScrollToTopOrBottomCallback onScrollToTopOrBottomCallback;
//    private OnScrolledToBottomCallback onScrolledToBottomCallback;
//
//    private final int ORIGINAL_COLUMNS = 0; // 默认固定的列数
//    private int fixedColumnByLast = ORIGINAL_COLUMNS;
//    private boolean isRemoveView = false; // 进行固定列的View填充前，是否需要删除已有的View
//
//    protected RecyclerView recyclerView;
//    protected RecyclerView headerRecyclerView;
//    protected PanelLineAdapter panelLineAdapter;
//    protected PanelAdapter panelAdapter;
//    protected FrameLayout firstItemView;
//    protected LinearLayout lastItemView;
//
//    public ScrollablePanel(Context context, PanelAdapter panelAdapter) {
//        super(context);
//        this.panelAdapter = panelAdapter;
//        initView();
//    }
//
//    public ScrollablePanel(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        initView();
//    }
//
//    public ScrollablePanel(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView();
//    }
//
//    public void setFixedColumnByLast(int fixedColumnByLast) {
//        this.fixedColumnByLast = fixedColumnByLast;
//    }
//
//    private void initView() {
//        LayoutInflater.from(getContext()).inflate(R.layout.view_scrollable_panel, this, true);
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_content_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        firstItemView = (FrameLayout) findViewById(R.id.first_item);
//        lastItemView = (LinearLayout) findViewById(R.id.last_item);
//        headerRecyclerView = (RecyclerView) findViewById(R.id.recycler_header_list);
//        headerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        headerRecyclerView.setHasFixedSize(true);
//        if (panelAdapter != null) {
//            panelLineAdapter = new PanelLineAdapter(panelAdapter, recyclerView, headerRecyclerView);
//            recyclerView.setAdapter(panelLineAdapter);
//            setUpFirstItemView(panelAdapter);
//            setUpLastItemView(panelAdapter);
//        }
//
//        // 问题代码
////        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
////
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
////                super.onScrollStateChanged(recyclerView, newState);
////                // lidachen: 这里尝试将RecyclerView滚动状态由 SCROLL_STATE_IDLE -> SCROLL_STATE_SETTlING
////                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
////                    boolean isToTop = recyclerView.canScrollVertically(1);
////                    boolean isToBottom = recyclerView.canScrollVertically(-1);
////                    if (onScrollToTopOrBottomCallback != null)
////                        onScrollToTopOrBottomCallback.onScrollToTopOrBottom(isToTop, isToBottom);
////
////                    if (onScrolledToBottomCallback != null) {
////                        // 获取最后一个完全显示的ItemPosition
////                        int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
////                        // 获取条目的总数
////                        int totalItemCount = layoutManager.getItemCount();
////
////                        if (isToBottom && lastVisibleItem == totalItemCount -1)
////                            onScrolledToBottomCallback.onScrolledToBottom(true);
////                         else
////                            onScrolledToBottomCallback.onScrolledToBottom(false);
////                    }
////
////                }
////
////
////            }
////
////            @Override
////            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
////                super.onScrolled(recyclerView, dx, dy);
////                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
////                //dx>0:向右滑动,dx<0:向左滑动
////                //dy>0:向下滑动,dy<0:向上滑动
//////                if (dy > 0) {
//////                    isSlidingToLast = true;
//////                } else {
//////                    isSlidingToLast = false;
//////                }
////            }
////        });
//    }
//
//    private void setUpFirstItemView(PanelAdapter panelAdapter) {
//        RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(firstItemView, panelAdapter.getItemViewType(0, 0));
//        panelAdapter.onBindViewHolder(viewHolder, 0, 0);
//        firstItemView.addView(viewHolder.itemView);
//    }
//
//    private void setUpLastItemView(PanelAdapter panelAdapter) {
//        if (fixedColumnByLast == 0) return;
//        if (isRemoveView) {
//            removeLastItemView(panelAdapter);
//        }
//
//        for (int i = 0; i < fixedColumnByLast; i++) {
//            int realIndex = panelAdapter.getColumnCount() - (fixedColumnByLast - i);
//            RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(lastItemView, panelAdapter.getItemViewType(0, realIndex));
//            panelAdapter.onBindViewHolder(viewHolder, 0, realIndex);
//            lastItemView.addView(viewHolder.itemView, i);
//        }
//    }
//
//    private void removeLastItemView(PanelAdapter panelAdapter) {
//        lastItemView.removeAllViews();
//    }
//
//    public void notifyDataSetChanged() {
//        if (panelLineAdapter != null) {
//            setUpFirstItemView(panelAdapter);
//            setUpLastItemView(panelAdapter);
//            panelLineAdapter.notifyDataChanged();
//        }
//    }
//
//    /**
//     * @param panelAdapter {@link PanelAdapter}
//     */
//    public void setPanelAdapter(PanelAdapter panelAdapter) {
//        if (this.panelLineAdapter != null) {
//            panelLineAdapter.setPanelAdapter(panelAdapter);
//            panelLineAdapter.notifyDataSetChanged();
//        } else {
//            panelLineAdapter = new PanelLineAdapter(fixedColumnByLast, panelAdapter, recyclerView, headerRecyclerView);
//            recyclerView.setAdapter(panelLineAdapter);
//        }
//        this.panelAdapter = panelAdapter;
//        setUpFirstItemView(panelAdapter);
//        setUpLastItemView(panelAdapter);
//    }
//
//    /**
//     * 处理表格最后的固定列
//     * @param fixedColumnByLast 固定的列数
//     */
//    public void handleFixedColumnByLast(int fixedColumnByLast) {
//        // 当前固定列与预期固定列一样，不做操作
//        if (this.fixedColumnByLast == fixedColumnByLast) return;
//
//        isRemoveView = fixedColumnByLast != 0;
//        this.fixedColumnByLast = fixedColumnByLast;
//
//        if (this.panelLineAdapter != null) {
//            if (fixedColumnByLast == 0) {
//                removeLastItemView(panelAdapter);
//            } else {
//                setUpLastItemView(panelAdapter);
//            }
//            panelLineAdapter.setFixedColumnByLastParams(fixedColumnByLast, isRemoveView);
//            panelLineAdapter.setUpHeaderRecyclerView();
//            panelLineAdapter.notifyDataSetChanged();
//        }
//        isRemoveView = false;
//    }
//
//    /**
//     * Adapter used to bind dataSet to cell View that are displayed within every row of {@link ScrollablePanel}.
//     */
//    private static class PanelLineItemAdapter extends RecyclerView.Adapter {
//
//        private PanelAdapter panelAdapter;
//        private int row;
//        // 表格最后固定了几列
//        private int fixedColumnByLast = 0;
//
//        public PanelLineItemAdapter(int row, int fixedColumnByLast, PanelAdapter panelAdapter) {
//            this.row = row;
//            this.panelAdapter = panelAdapter;
//            this.fixedColumnByLast = fixedColumnByLast;
//        }
//
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return this.panelAdapter.onCreateViewHolder(parent, viewType);
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            this.panelAdapter.onBindViewHolder(holder, row, position + 1);
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return this.panelAdapter.getItemViewType(row, position + 1);
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return panelAdapter.getColumnCount() - 1 - fixedColumnByLast;
//        }
//
//        public void setRow(int row) {
//            this.row = row;
//        }
//
//        public void setFixedColumnByLast(int fixedColumnByLast) {
//            this.fixedColumnByLast = fixedColumnByLast;
//        }
//    }
//
//
//    /**
//     * Adapter used to bind dataSet to views that are displayed within a{@link ScrollablePanel}.
//     */
//    private static class PanelLineAdapter extends RecyclerView.Adapter<PanelLineAdapter.ViewHolder> {
//
//        private PanelAdapter panelAdapter;
//        private RecyclerView headerRecyclerView;
//        private RecyclerView contentRV;
//        private HashSet<RecyclerView> observerList = new HashSet<>();
//        private int firstPos = -1;
//        private int firstOffset = -1;
//        private int fixedColumnByLast = 0;
//        private boolean isRemoveView = false;
//
//        public PanelLineAdapter(PanelAdapter panelAdapter, RecyclerView contentRV, RecyclerView headerRecyclerView) {
//            this(0, panelAdapter, contentRV, headerRecyclerView);
//        }
//
//        public PanelLineAdapter(int fixedColumnByLast, PanelAdapter panelAdapter, RecyclerView contentRV, RecyclerView headerRecyclerView) {
//            this.fixedColumnByLast = fixedColumnByLast;
//            this.panelAdapter = panelAdapter;
//            this.headerRecyclerView = headerRecyclerView;
//            this.contentRV = contentRV;
//            initRecyclerView(headerRecyclerView);
//            setUpHeaderRecyclerView();
//        }
//
//        public void setPanelAdapter(PanelAdapter panelAdapter) {
//            this.panelAdapter = panelAdapter;
//            setUpHeaderRecyclerView();
//        }
//
//        /**
//         * 设置固定列参数
//         * @param fixedColumnByLast 固定列数
//         * @param isRemoveView 是否在填充View前，移除原有View
//         */
//        public void setFixedColumnByLastParams(int fixedColumnByLast, boolean isRemoveView) {
//            this.fixedColumnByLast = fixedColumnByLast;
//            this.isRemoveView = isRemoveView;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public int getItemCount() {
//            return panelAdapter.getRowCount();
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.listitem_content_row, parent, false));
//            initRecyclerView(viewHolder.recyclerView);
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            PanelLineItemAdapter lineItemAdapter = (PanelLineItemAdapter) holder.recyclerView.getAdapter();
//            if (lineItemAdapter == null) {
//                lineItemAdapter = new PanelLineItemAdapter(position + 1, fixedColumnByLast, panelAdapter);
//                holder.recyclerView.setAdapter(lineItemAdapter);
//            } else {
//                lineItemAdapter.setRow(position + 1);
//                lineItemAdapter.setFixedColumnByLast(fixedColumnByLast);
//                lineItemAdapter.notifyDataSetChanged();
//            }
//            // 第一列
//            if (holder.firstColumnItemVH == null) {
//                RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(holder.firstColumnItemView, panelAdapter.getItemViewType(position + 1, 0));
//                holder.firstColumnItemVH = viewHolder;
//                panelAdapter.onBindViewHolder(holder.firstColumnItemVH, position + 1, 0);
//                holder.firstColumnItemView.addView(viewHolder.itemView);
//            } else {
//                panelAdapter.onBindViewHolder(holder.firstColumnItemVH, position + 1, 0);
//            }
//
//            // 最后的列
//            if (fixedColumnByLast != 0) {
//                if (isRemoveView) {
//                    holder.lastViewHolders.clear();
//                    holder.lastColumnItemView.removeAllViews();
//                }
////                LogUtils.i("isRemoveView", isRemoveView + "");
//
//                if (holder.lastViewHolders.size() < fixedColumnByLast) {
//                    for (int i = 0; i < fixedColumnByLast; i++) {
//                        int realIndex = panelAdapter.getColumnCount() - (fixedColumnByLast - i);
//                        RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(holder.lastColumnItemView, panelAdapter.getItemViewType(position + 1, realIndex));
//                        holder.lastViewHolders.add(viewHolder);
//                        panelAdapter.onBindViewHolder(viewHolder, position + 1, realIndex);
//                        holder.lastColumnItemView.addView(viewHolder.itemView, i);
//                    }
//                } else {
//                    for (int i = 0; i < fixedColumnByLast; i++) {
//                        int realIndex = panelAdapter.getColumnCount() - (fixedColumnByLast - i);
//                        panelAdapter.onBindViewHolder(holder.lastViewHolders.get(i), position + 1, realIndex);
//                    }
//                }
//
////                if (holder.lastColumnItemVH == null) {
////                    RecyclerView.ViewHolder viewHolder = panelAdapter.onCreateViewHolder(holder.lastColumnItemView, panelAdapter.getItemViewType(position + 1, panelAdapter.getColumnCount() - 1));
////                    holder.lastColumnItemVH = viewHolder;
////                    panelAdapter.onBindViewHolder(holder.lastColumnItemVH, position + 1, panelAdapter.getColumnCount() - 1);
////                    holder.lastColumnItemView.addView(viewHolder.itemView);
////                } else {
////                    panelAdapter.onBindViewHolder(holder.lastColumnItemVH, position + 1, panelAdapter.getColumnCount() - 1);
////                }
//            } else {
////                holder.lastColumnItemVH = null;
//
//                holder.lastViewHolders.clear();
//                holder.lastColumnItemView.removeAllViews();
//            }
//        }
//
//
//        public void notifyDataChanged() {
//            setUpHeaderRecyclerView();
//            notifyDataSetChanged();
//        }
//
//
//        private void setUpHeaderRecyclerView() {
//            if (panelAdapter != null) {
//                if (headerRecyclerView.getAdapter() == null) {
//                    PanelLineItemAdapter lineItemAdapter = new PanelLineItemAdapter(0, fixedColumnByLast, panelAdapter);
//                    headerRecyclerView.setAdapter(lineItemAdapter);
//                } else {
//                    PanelLineItemAdapter adapter = (PanelLineItemAdapter) headerRecyclerView.getAdapter();
//                    adapter.setFixedColumnByLast(fixedColumnByLast);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        }
//
//        public void initRecyclerView(RecyclerView recyclerView) {
//            recyclerView.setHasFixedSize(true);
//            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            if (layoutManager != null && firstPos > 0 && firstOffset > 0) {
//                layoutManager.scrollToPositionWithOffset(PanelLineAdapter.this.firstPos + 1, PanelLineAdapter.this.firstOffset);
//            }
//            observerList.add(recyclerView);
//            recyclerView.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    switch (motionEvent.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                        case MotionEvent.ACTION_POINTER_DOWN:
//                            for (RecyclerView rv : observerList) {
//                                rv.stopScroll();
//                            }
//                    }
//                    return false;
//                }
//            });
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    int firstPos = linearLayoutManager.findFirstVisibleItemPosition();
//                    View firstVisibleItem = linearLayoutManager.getChildAt(0);
//                    if (firstVisibleItem != null) { // 第一个可见的Item
//                        int firstRight = linearLayoutManager.getDecoratedRight(firstVisibleItem);
//                        for (RecyclerView rv : observerList) {
//                            if (recyclerView != rv) {
//                                LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
//                                if (layoutManager != null) {
//                                    PanelLineAdapter.this.firstPos = firstPos;
//                                    PanelLineAdapter.this.firstOffset = firstRight;
//                                    layoutManager.scrollToPositionWithOffset(firstPos + 1, firstRight);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                }
//            });
//        }
//
//        private HashSet<RecyclerView> getRecyclerViews() {
//            HashSet<RecyclerView> recyclerViewHashSet = new HashSet<>();
//            recyclerViewHashSet.add(headerRecyclerView);
//
//            for (int i = 0; i < contentRV.getChildCount(); i++) {
//                recyclerViewHashSet.add((RecyclerView) contentRV.getChildAt(i).findViewById(R.id.recycler_line_list));
//            }
//            return recyclerViewHashSet;
//        }
//
//
//        static class ViewHolder extends RecyclerView.ViewHolder {
//            public RecyclerView recyclerView;
//            public FrameLayout firstColumnItemView;
//            public LinearLayout lastColumnItemView;
//            public RecyclerView.ViewHolder firstColumnItemVH;
//            public RecyclerView.ViewHolder lastColumnItemVH;
//            public List<RecyclerView.ViewHolder> lastViewHolders = new ArrayList<>();
//
//            public ViewHolder(View view) {
//                super(view);
//                this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_line_list);
//                this.firstColumnItemView = (FrameLayout) view.findViewById(R.id.first_column_item);
//                this.lastColumnItemView = (LinearLayout) view.findViewById(R.id.last_column_item);
//                this.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
//            }
//        }
//
//    }
//
//    /*
//        滑动到底部到顶部 (进行中)
//     */
//    public interface OnScrollToTopOrBottomCallback {
//        void onScrollToTopOrBottom(boolean isToTop, boolean isToBottom);
//    }
//
//    /**
//     * 是否滑动到最后一个Item回调
//     */
//    public interface OnScrolledToBottomCallback {
//        /**
//         * @param isScrolled 是否滑动到底部
//         */
//        void onScrolledToBottom(boolean isScrolled);
//    }
//
//    /**
//     * @return 当前软键盘是否打开
//     */
//    private static boolean isSoftInputShow(Context context) {
//        Activity activity = (Activity)context;
//        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
//        Log.i("AAA", "isSoftInputShow: -> " + isKeyboardShown(rootView));
//        return isKeyboardShown(rootView);
//    }
//
//    private static boolean isKeyboardShown(View rootView) {
//        final int softKeyboardHeight = 100;
//        Rect r = new Rect();
//        rootView.getWindowVisibleDisplayFrame(r);
//        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
//        int heightDiff = rootView.getBottom() - r.bottom;
//        return heightDiff > softKeyboardHeight * dm.density;
//    }
//}
