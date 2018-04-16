package d.d4enterprises.androidhiverxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MultiObserversAndCompositeDisposables extends AppCompatActivity {

    /**
     * A disposable is used to dispose the Subscription when an Observer no longer
     * wants to listen to the Observable
     */

    private int count;

    ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    ListView listView;

    private CompositeDisposable compositeDisposable;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_observers_and_composite_disposables);

        compositeDisposable = new CompositeDisposable();

        listView = findViewById(R.id.list_item);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);

        listView.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private Observable<ArrayList<String>> getAnimalObservable() {
        return Observable.fromCallable(() -> getArrayList());
    }

    private Observable<ArrayList<String>> getAnimalAllCapsObservable() {
        return Observable.fromCallable(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                return getArrayList();
            }
        });

    }

    private DisposableObserver<String> getAnimalObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
                addItem("onNext               -> " + value);
                Log.d(TAG, "onNext               -> " + value);
            }

            @Override
            public void onComplete() {
                addItem("Completed");
                Log.d(TAG, "Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                addItem("Error    " + throwable.getMessage());
                Log.e(TAG, "Error    " + throwable.getMessage());
            }

        };
    }

    private DisposableObserver<String> getAnimalAllCapsObserver() {
        return new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                addItem("onNext               -> " + value.toUpperCase());
                Log.d(TAG, "onNext               -> " + value);
            }

            @Override
            public void onComplete() {
                addItem("Completed");
                Log.d(TAG, "Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                addItem("Error    " + throwable.getMessage());
                Log.e(TAG, "Error    " + throwable.getMessage());
            }
        };
    }


    public void subscribe1(View view) {

        /**
         * flatMapIterable is required for filter to perform a predicateTest over the Iterable items.
         *
         * We use {@link Observable#subscribeWith(Observer)} instead of {@link Observable#subscribe(Observer)}
         */
        compositeDisposable.add(getAnimalObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable((ArrayList<String> arrayList) -> {
                    Iterable<String> iterable = arrayList;
                    return iterable;
                })
                .filter(value -> (value.toLowerCase().startsWith("q") || value.toLowerCase().startsWith("b")))
                .subscribeWith(getAnimalObserver()));
    }

    public void subscribe2(View view) {
        compositeDisposable.add(getAnimalAllCapsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable((ArrayList<String> arrayList) -> arrayList)
                .subscribeWith(getAnimalAllCapsObserver()));
    }

    private void addItem(String text) {
        arrayList.add(text);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        scrollToBottom();
    }


    private ArrayList<String> getArrayList() {
        String[] strings = {"Ant", "Bee", "Cow", "Dog", "Fox"
                , "Lol", "Boxer", "Babool", "Quala"};

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public void showText(View view) {
        addItem("Clicked the button" + count++);
    }

    public void clearList(View view) {
        count = 0;
        arrayList.clear();
        adapter.notifyDataSetChanged();
    }

    private void scrollToBottom() {
        listView.smoothScrollToPosition(arrayList.size() - 1);
    }
}
