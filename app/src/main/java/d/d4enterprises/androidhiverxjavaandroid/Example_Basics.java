package d.d4enterprises.androidhiverxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * /**
 * Basic Observable, Observer, Subscriber example
 * Observable emits list of animal names
 * You can see filter() operator is used to filter out the animal names
 */
public class Example_Basics extends AppCompatActivity {

    /**
     * A disposable is used to dispose the Subscription when an Observer no longer
     * wants to listen to the Observable
     */
    Disposable disposable;

    private int count;

    ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    ListView listView;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example__basics);

        listView = findViewById(R.id.list_item);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);

        listView.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    private Observable<ArrayList<String>> getAnimalObservable() {
        //returning Observable.just() will freeze the UI
        //return Observable.just(getArrayList());

/*        return Observable.fromCallable(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                return getArrayList();
            }
        });*/

        return Observable.fromCallable(() -> getArrayList());
    }

    private Observer<String> getAnimalObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                Example_Basics.this.disposable = disposable;
                addItem("onSubscribe");
                Log.d(TAG, "onSubscribe");
            }

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

    private void addItem(String text) {
        arrayList.add(text);
        if (adapter != null)
            adapter.notifyDataSetChanged();
        scrollToBottom();
    }

    public void subscribe(View view) {
        /*
            .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        //return (s.toLowerCase().startsWith("q") || s.toLowerCase().startsWith("b"));
                        return true;
                    }
                })

                .flatMapIterable(new Function<ArrayList<String>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(ArrayList<String> value) throws Exception {
                        Iterable<String> iterable = value;
                        return iterable;
                    }
                })
        */

        /**
         * flatMapIterable is required for filter to perform a predicateTest over the Iterable items.
         */
        getAnimalObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable((ArrayList<String> arrayList) -> {
                    Iterable<String> iterable = arrayList;
                    return iterable;
                })
                .filter(value -> (value.toLowerCase().startsWith("q") || value.toLowerCase().startsWith("b")))
                .subscribe(getAnimalObserver());
    }


    private ArrayList<String> getArrayList() {
        String[] strings = {"Ant", "Bee", "Cow", "Dog", "Fox"
                , "Lol", "Boxer", "Babool", "Quala"};

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
            try {
                Thread.sleep(500);
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
