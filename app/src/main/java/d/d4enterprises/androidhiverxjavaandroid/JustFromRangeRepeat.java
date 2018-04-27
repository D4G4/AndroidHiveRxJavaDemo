package d.d4enterprises.androidhiverxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class JustFromRangeRepeat extends AppCompatActivity {

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

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_just_from_range_repeat);

    listView = findViewById(R.id.list_item);

    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
        arrayList);

    listView.setAdapter(adapter);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (disposable != null) disposable.dispose();
  }

  private void addItem(String text) {
    arrayList.add(text);
    if (adapter != null) adapter.notifyDataSetChanged();
    scrollToBottom();
  }

  private ArrayList<String> getArrayList() {
    String[] strings = {
        "Ant", "Bee", "Cow", "Dog", "Fox", "Lol", "Boxer", "Babool", "Quala"
    };

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

  //region ClickListeners

  /**
   * Observable.just() can take up to 10 arguments
   */
  public void eg1(View view) {
    Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Integer integer) {
            addItem("onNext " + integer);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  /**
   * Observable.just can also take a collection.
   */
  public void eg2(View view) {
    Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    Observable.just(numbers)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer[]>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Integer[] integer) {
            addItem("On next");
            //You'll have to loop through the array because Observer is of type Integer[]
            for (int i = 0; i < integer.length; i++) {
              addItem("Iteration " + integer[i]);
            }
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  /**
   * From() creates an Observable from set of items using an Iterable, i.e.
   * i.e. each item is emitted one at a time
   */
  public void eg3(View view) {
    Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {
            addItem("Observable.fromArray()");
          }

          @Override public void onNext(Integer integer) {
            addItem("onNext " + integer);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  /**
   * Repeat() creates an Observable that emits an item or series of items repeatedly
   */
  public void eg4(View view) {
    Observable.range(0, 10)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {
            addItem("Observable.range()");
          }

          @Override public void onNext(Integer integer) {
            addItem("onNext " + integer);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  public void eg5(View view) {
    Observable.range(1, 4)
        .repeat(4)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {
            addItem("Observable.range()");
          }

          @Override public void onNext(Integer integer) {
            addItem("onNext " + integer);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  public void eg6(View view) {
    Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Integer integer) {
            addItem("onNext " + integer);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  public void subscribe(View view) {
  }
  //endregion
}


