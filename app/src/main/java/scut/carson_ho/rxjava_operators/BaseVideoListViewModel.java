package scut.carson_ho.rxjava_operators;

import android.util.Log;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @param <P>
 * @param <M>
 * @param <V>
 */
public abstract class BaseVideoListViewModel<P, M, V extends String> {
    private static final String TAG = "";

    @Nullable
    private P mResponse;
    @Nullable
    private M mDataMode;

    private boolean mHasMore = true;
    private int mPage = 1;
    private int mOffset;

    protected abstract Single<P> loadInner(String str, Map<String, String> map);

    protected abstract M parseResponseModel(P p);

    @Nullable
    protected abstract List<V> parseVideoList(@Nullable M m);

    protected abstract boolean parseHasMore(@Nullable M m);

    protected abstract int parseOffset(@Nullable M m);

    @Nullable
    public Disposable load(String source, Map<String, String> param, final boolean isLoadingMore) {
        return loadInner(source, param).subscribeOn(Schedulers.io())
//                .map(new Function<P, List<V>>() {
//                    @Override
//                    public List<V> apply(P p) {
//                        return BaseVideoListViewModel.this.parseResponse(p);
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<P, List<V>>() {
                    @Override
                    public List<V> apply(P p) {
                        return BaseVideoListViewModel.this.parseResponse(p);
                    }
                })

//                .subscribe(new Consumer<P>() {
//                    @Override
//                    public void accept(P p) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });

                .subscribe(); // 内部本质参数实现为空对象： return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING);
//                .subscribe(new Consumer<List<V>>() {
//                    @Override
//                    public void accept(List<V> vs) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
    }


    @Nullable
    public List<V> parseResponse(P p) {
        this.mResponse = p;
        this.mDataMode = parseResponseModel(p);
        parsePageInfo(this.mDataMode);
        return parseVideoList(this.mDataMode);
    }


    protected void parsePageInfo(@Nullable M m) {
        this.mPage++;
        this.mOffset = parseOffset(m);
        this.mHasMore = parseHasMore(m);
        Log.d(TAG, "parsePageInfo.mHasMore = " + this.mHasMore);
    }

}
