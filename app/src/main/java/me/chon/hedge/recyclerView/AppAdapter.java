package me.chon.hedge.recyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.chon.hedge.R;
import me.chon.hedge.activity.DetailActivity;
import me.chon.hedge.activity.MainActivity;
import me.chon.hedge.entity.MovieEntity;
import me.chon.hedge.http.ProgressSubscriber;
import me.chon.hedge.widgets.pull.BaseListAdapter;
import me.chon.hedge.widgets.pull.BaseViewHolder;
import me.chon.hedge.widgets.pull.layoutmanager.MyLinearLayoutManager;

/**
 * Created by chon on 2017/2/23.
 * What? How? Why?
 */

public class AppAdapter extends BaseListAdapter {
    private final Context context;
    private final List<PackageInfo> data;

    public AppAdapter(Context context, List<PackageInfo> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    protected int getDataCount() {
        return data.size();
    }

    @Override
    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_app,null);
        return new AppHolder(itemView,data);
    }

    private static class AppHolder extends BaseViewHolder {
        private List<PackageInfo> data;
        ImageView logo;
        TextView name;
        TextView version;
//        TextView tv_pkg_name;
//        TextView tv_md5;
//        SparseArray<String> md5SparseArray;
          Map<Integer,Drawable> drawableMap = new HashMap<>();

        AppHolder(View itemView, List<PackageInfo> data) {
            super(itemView);
//            md5SparseArray = new SparseArray<>();
            this.data = data;
            logo = (ImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.tv_app_name);
            version = (TextView) itemView.findViewById(R.id.tv_app_version);
//            tv_pkg_name = (TextView) itemView.findViewById(R.id.tv_pkg_name);
//            tv_md5 = (TextView) itemView.findViewById(R.id.tv_md5);
        }

        @Override
        public void onBindViewHolder(int position) {
            PackageInfo packageInfo = data.get(position);

            name.setText(packageInfo.applicationInfo.loadLabel(itemView.getContext().getPackageManager()));
            version.setText(packageInfo.versionName + "\n" + String.valueOf(packageInfo.versionCode));
//            tv_pkg_name.setText(packageInfo.packageName);

            Drawable drawable = drawableMap.get(position);
            if (drawable == null) {
                drawable = packageInfo.applicationInfo.loadIcon(itemView.getContext().getPackageManager());
                drawableMap.put(position,drawable);
            }
            Glide.with(itemView.getContext()).load("").placeholder(drawable).into(logo);
//            logo.setImageDrawable(drawable);


//            String md5 = md5SparseArray.get(position);
//            if (TextUtils.isEmpty(md5)) {
//                tv_md5.setTextColor(Color.BLACK);
//                tv_md5.setText("还没获取MD5");
//            } else {
//                tv_md5.setTextColor(Color.RED);
//                tv_md5.setText(String.format("MD5:%s",md5));
//            }
        }

        @Override
        public void onItemClick(View view, int position) {
//            if (!TextUtils.isEmpty(md5SparseArray.get(position))) {
//                Toast.makeText(view.getContext(), "MD5 already got", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            tv_md5.setTextColor(Color.BLACK);
//            tv_md5.setText("获取MD5 ing...");

            final PackageInfo packageInfo = data.get(position);

            Observable.just(packageInfo.applicationInfo.sourceDir)
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            return md5sum(s);
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<String>(view.getContext()) {
                        @Override
                        public void onNext(String md5) {
//                            md5SparseArray.put(position,md5);
//                            tv_md5.setTextColor(Color.RED);
//                            tv_md5.setText(String.format("MD5:%s",md5));

                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setTitle(packageInfo.applicationInfo.loadLabel(itemView.getContext().getPackageManager()));
                            builder.setMessage(packageInfo.packageName + "\n" + md5);
                            builder.create().show();
                        }
                    });
        }

        private String md5sum(String filename) {
            InputStream fis = null;
            byte[] buffer = new byte[1024];
            int numRead = 0;
            MessageDigest md5 = null;

            try {

                fis = new FileInputStream(filename);
                md5 = MessageDigest.getInstance("MD5");
                while ((numRead = fis.read(buffer)) > 0) {
                    md5.update(buffer, 0, numRead);
                }
                return parseByte2HexStr(md5.digest());

            } catch (Exception e) {
                e.printStackTrace();
                return "";
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        private String parseByte2HexStr(byte buf[]) {
            if (buf == null) {
                return "";
            }

            StringBuffer sb = new StringBuffer();
            for (byte aBuf : buf) {
                String hex = Integer.toHexString(aBuf & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }
    }
}
