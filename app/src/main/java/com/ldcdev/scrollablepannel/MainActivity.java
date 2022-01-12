package com.ldcdev.scrollablepannel;

import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ldcdev.library.ScrollablePanel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final SimpleDateFormat DAY_UI_MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEE", Locale.US);

    private List<Button> buttonList = new ArrayList<>();
    private boolean isLastColumnFixed = false;
    private ScrollablePanel scrollablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLast = findViewById(R.id.btnLast);
        buttonList.add(btnLast);
        Button btnLast2 = findViewById(R.id.btnLast2);
        buttonList.add(btnLast2);
        Button btn5To6 = findViewById(R.id.btn5To6);
        buttonList.add(btn5To6);
        Button btnRandom3 = findViewById(R.id.btnRandom3);
        buttonList.add(btnRandom3);

        scrollablePanel = (ScrollablePanel) findViewById(R.id.scrollable_panel);
        final ScrollablePanelAdapter scrollablePanelAdapter = new ScrollablePanelAdapter();
        generateTestData(scrollablePanelAdapter);
        scrollablePanel.setPanelAdapter(scrollablePanelAdapter);
    }

    private void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {
        List<RoomInfo> roomInfoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomType("SUPK");
            roomInfo.setRoomId(i);
            roomInfo.setRoomName("20" + i);
            roomInfoList.add(roomInfo);
        }
        for (int i = 6; i < 30; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomType("Standard");
            roomInfo.setRoomId(i);
            roomInfo.setRoomName("30" + i);
            roomInfoList.add(roomInfo);
        }
        scrollablePanelAdapter.setRoomInfoList(roomInfoList);

        List<DateInfo> dateInfoList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 14; i++) {
            DateInfo dateInfo = new DateInfo();
            String date = DAY_UI_MONTH_DAY_FORMAT.format(calendar.getTime());
            String week = WEEK_FORMAT.format(calendar.getTime());
            dateInfo.setDate(date);
            dateInfo.setWeek(week);
            dateInfoList.add(dateInfo);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        scrollablePanelAdapter.setDateInfoList(dateInfoList);

        List<List<OrderInfo>> ordersList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            List<OrderInfo> orderInfoList = new ArrayList<>();
            for (int j = 0; j < 14; j++) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGuestName("NO." + i + j);
                orderInfo.setBegin(true);
                orderInfo.setStatus(OrderInfo.Status.randomStatus());
                if (orderInfoList.size() > 0) {
                    OrderInfo lastOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
                    if (orderInfo.getStatus().ordinal() == lastOrderInfo.getStatus().ordinal()) {
                        orderInfo.setId(lastOrderInfo.getId());
                        orderInfo.setBegin(false);
                        orderInfo.setGuestName("");
                    } else {
                        if (new Random().nextBoolean()) {
                            orderInfo.setStatus(OrderInfo.Status.BLANK);
                        }
                    }
                }
                orderInfoList.add(orderInfo);
            }
            ordersList.add(orderInfoList);
        }
        scrollablePanelAdapter.setOrdersList(ordersList);
    }

    public void handleFixedColumnByLast(View view) {
        Button b = (Button) view;
        changeButtonStatus(b);

        if (scrollablePanel != null) {
            if (isLastColumnFixed) {
                b.setText("固定最后3列");
                scrollablePanel.handleFixedColumnByLast(0);
            } else {
                b.setText("取消固定");
                scrollablePanel.handleFixedColumnByLast(3);
            }
            isLastColumnFixed = !isLastColumnFixed;
        }
    }

    public void handleFixedColumnByLast2(View view) {
        Button b = (Button) view;
        changeButtonStatus(b);

        if (scrollablePanel != null) {
            if (isLastColumnFixed) {
                b.setText("固定最后3列_plan2");
                scrollablePanel.handleFixedAssignColumns(new int[0]);
            } else {
                b.setText("取消固定");
                scrollablePanel.handleFixedAssignColumns(new int[]{12, 13, 14});
            }
            isLastColumnFixed = !isLastColumnFixed;
        }
    }

    public void handleFixedColumn5To6(View view) {
        Button b = (Button) view;
        changeButtonStatus(b);

        if (scrollablePanel != null) {
            if (isLastColumnFixed) {
                b.setText("固定5-6列");
                scrollablePanel.handleFixedAssignColumns(new int[0]);
            } else {
                b.setText("取消固定");
                scrollablePanel.handleFixedAssignColumns(new int[]{5, 6});
            }
            isLastColumnFixed = !isLastColumnFixed;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void handleFixed3ColumnRandom(View view) {
        Button b = (Button) view;
        changeButtonStatus(b);

        if (scrollablePanel != null) {
            if (isLastColumnFixed) {
                b.setText("随机固定3列");
                scrollablePanel.handleFixedAssignColumns(new int[0]);
            } else {
                b.setText("取消固定");
                ArraySet<Integer> set = new ArraySet<>();
                do {
                    int nextInt = new Random().nextInt(13);
                    if (nextInt != 0) {
                        set.add(nextInt);
                    }
                } while (set.size() != 3);
                Iterator<Integer> it = set.iterator();
                scrollablePanel.handleFixedAssignColumns(new int[]{it.next(), it.next(), it.next()});
            }
            isLastColumnFixed = !isLastColumnFixed;
        }
    }

    private void changeButtonStatus(Button button) {
        for (Button el : buttonList) {
            if (!el.equals(button)) {
                el.setVisibility(View.GONE);

                if (isLastColumnFixed) el.setVisibility(View.VISIBLE);
            }
        }
    }
}
