package d.d4enterprises.toptalrxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Observable<ArrayList<String>> animalObservable;
    Observer<String> animalObserver;

    /**
     * A disposable is used to dispose the Subscription when an Observer no longer
     * wants to listen to the Observable
     */
    Disposable disposable;

    ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    ListView listView;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_item);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, arrayList);

        listView.setAdapter(adapter);

        animalObservable = getAnimalObservable();

        animalObserver = getAnimalObserver();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private Observable<ArrayList<String>> getAnimalObservable() {
        return Observable.just(getArrayList());
    }

    private Observer<String> getAnimalObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                MainActivity.this.disposable = disposable;
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

        animalObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable((ArrayList<String> arrayList) -> {
                    Iterable<String> iterable = arrayList;
                    return iterable;
                })
                .filter(value -> (value.toLowerCase().startsWith("q") || value.toLowerCase().startsWith("b")))
                .subscribe(animalObserver);
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
}
