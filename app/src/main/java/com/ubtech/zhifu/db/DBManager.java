package com.ubtech.zhifu.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubtech.zhifu.bean.OrderBean;

import java.util.ArrayList;

/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class DBManager {
    private SQLiteDatabase db = this.helper.getWritableDatabase();
    private DBHelper helper;

    public DBManager(Context context) {
        this.helper = new DBHelper(context);
    }

    public void addOrder(OrderBean ordereBean) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO payorder VALUES(null,?,?,?,?)", new Object[]{ordereBean.getMoney(), ordereBean.getNo(), ordereBean.getResult(), ordereBean.getPayuser()});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public boolean isExistAliBOrder(String no) {
        boolean isExist = false;
        Cursor c = ExecSQLForCursor("SELECT * FROM payorder WHERE tradeno='" + no + "'");
        if (c.getCount() > 0) {
            isExist = true;
        }
        c.close();
        return isExist;
    }

    public void updateOrder(String no, String result) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("UPDATE payorder SET result=? WHERE tradeno=?", new Object[]{result, no});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public ArrayList<OrderBean> FindAllOrders() {
        ArrayList<OrderBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor("SELECT * FROM payorder where result <> 'success'");
        while (c.moveToNext()) {
            OrderBean info = new OrderBean();
            info.setMoney(c.getString(c.getColumnIndex("money")));
            info.setNo(c.getString(c.getColumnIndex("tradeno")));
            info.setResult(c.getString(c.getColumnIndex("result")));
            info.setPayuser(c.getString(c.getColumnIndex("payuser")));
            list.add(info);
        }
        c.close();
        return list;
    }

    private Cursor ExecSQLForCursor(String sql) {
        return this.db.rawQuery(sql, (String[]) null);
    }

}
