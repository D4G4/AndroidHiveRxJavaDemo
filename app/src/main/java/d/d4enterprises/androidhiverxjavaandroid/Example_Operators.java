package d.d4enterprises.androidhiverxjavaandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.observable.ObservableError;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.observers.DisposableObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Basic Observable,Observer,Subscriber example
 * Introduced CompositeDisposable and DisposableObserver
 * The observable emits custom data types instead of primitives
 * --
 * .map() operator is used to turn the note into all uppercase letters
 * --
 * Here, we are going to use custom dataType
 * {@link Note}
 */
public class Example_Operators extends AppCompatActivity {

  ArrayAdapter<String> adapter;
  private ArrayList<String> arrayList = new ArrayList<>();
  ListView listView;
  private CompositeDisposable compositeDisposable;
  private int count;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_example__operators);

    compositeDisposable = new CompositeDisposable();

    listView = findViewById(R.id.list_item);

    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
        arrayList);

    listView.setAdapter(adapter);
  }

  public void subscribe1(View view) {
    compositeDisposable.add(getNotesObservableFreezeTheUi().map(new Function<Note, Note>() {
      @Override public Note apply(Note note) throws Exception {
        note.setNote(note.getNote().toUpperCase());
        return note;
      }
    }).subscribeWith(getNotesObserver()));
  }

  private DisposableObserver<Note> getNotesObserver() {
    return new DisposableObserver<Note>() {
      @Override public void onNext(Note note) {
        addItem(note.getNote());
      }

      @Override public void onError(Throwable e) {
        addItem(e.getMessage());
      }

      @Override public void onComplete() {
        addItem("Completed");
      }
    };
  }

  private Observable<Note> getNotesObservableFreezeTheUi() {
    return Observable.create(new ObservableOnSubscribe<Note>() {
      @Override public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
        final List<Note> notes = prepareNotes();
        for (Note note : notes) {
          if (!emitter.isDisposed()) {
            emitter.onNext(note);
          }
        }
        if (!emitter.isDisposed()) {
          emitter.onComplete();
        }
      }
    });

    /*return Observable.create(new ObservableOnSubscribe<Note>() {
      @Override public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
        new Thread(new Runnable() {
          @Override public void run() {
            final List<Note> notes = prepareNotes();
            for (Note note : notes) {
              if (!emitter.isDisposed()) {
                runOnUiThread(new Runnable() {
                  @Override public void run() {
                    emitter.onNext(note);
                  }
                });
              }
            }
            if (!emitter.isDisposed()) {
              emitter.onComplete();
            }
          }
        }).start();
      }
    });*/
  }
  //
  //private Observable<Note> getNotesObservable() {
  //
  //}

  private void addItem(String text) {
    arrayList.add(text);
    if (adapter != null) adapter.notifyDataSetChanged();
    scrollToBottom();
  }

  public void clearList(View view) {
    count = 0;
    arrayList.clear();
    adapter.notifyDataSetChanged();
  }

  private void scrollToBottom() {
    listView.smoothScrollToPosition(arrayList.size() - 1);
  }

  public void showText(View view) {
    addItem("Clicked the button" + count++);
  }

  private List<Note> prepareNotes() {
    List<Note> notes = new ArrayList<>();
    try {
      notes.add(new Note(1, "buy tooth paste!"));
      Thread.sleep(500);
      notes.add(new Note(2, "call brother!"));
      Thread.sleep(500);
      notes.add(new Note(3, "watch narcos tonight!"));
      Thread.sleep(500);
      notes.add(new Note(4, "pay power bill!"));
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return notes;
  }

  class Note {
    private int id;
    private String note;

    public Note(int id, String note) {
      this.id = id;
      this.note = note;
    }

    public int getId() {
      return id;
    }

    public String getNote() {
      return note;
    }

    public void setId(int id) {
      this.id = id;
    }

    public void setNote(String note) {
      this.note = note;
    }
  }
}
