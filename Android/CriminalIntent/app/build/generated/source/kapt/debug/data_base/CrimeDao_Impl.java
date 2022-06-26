package data_base;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bignerdranch.android.criminalintent.Crime;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CrimeDao_Impl implements CrimeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Crime> __insertionAdapterOfCrime;

  private final CrimeTypeConverters __crimeTypeConverters = new CrimeTypeConverters();

  private final EntityDeletionOrUpdateAdapter<Crime> __updateAdapterOfCrime;

  public CrimeDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCrime = new EntityInsertionAdapter<Crime>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Crime` (`id`,`title`,`date`,`isSolved`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Crime value) {
        final String _tmp = __crimeTypeConverters.fromUUID(value.getId());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        final Long _tmp_1 = __crimeTypeConverters.fromDate(value.getDate());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp_1);
        }
        final int _tmp_2 = value.isSolved() ? 1 : 0;
        stmt.bindLong(4, _tmp_2);
      }
    };
    this.__updateAdapterOfCrime = new EntityDeletionOrUpdateAdapter<Crime>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Crime` SET `id` = ?,`title` = ?,`date` = ?,`isSolved` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Crime value) {
        final String _tmp = __crimeTypeConverters.fromUUID(value.getId());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        final Long _tmp_1 = __crimeTypeConverters.fromDate(value.getDate());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp_1);
        }
        final int _tmp_2 = value.isSolved() ? 1 : 0;
        stmt.bindLong(4, _tmp_2);
        final String _tmp_3 = __crimeTypeConverters.fromUUID(value.getId());
        if (_tmp_3 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp_3);
        }
      }
    };
  }

  @Override
  public void addCrime(final Crime crime) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCrime.insert(crime);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCrime(final Crime crime) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCrime.handle(crime);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Crime>> getCrimes() {
    final String _sql = "SELECT * FROM Crime";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"Crime"}, false, new Callable<List<Crime>>() {
      @Override
      public List<Crime> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfIsSolved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSolved");
          final List<Crime> _result = new ArrayList<Crime>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Crime _item;
            final UUID _tmpId;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfId);
            }
            _tmpId = __crimeTypeConverters.toUUID(_tmp);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Date _tmpDate;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __crimeTypeConverters.toDate(_tmp_1);
            final boolean _tmpIsSolved;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSolved);
            _tmpIsSolved = _tmp_2 != 0;
            _item = new Crime(_tmpId,_tmpTitle,_tmpDate,_tmpIsSolved);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Crime> getCrime(final UUID id) {
    final String _sql = "SELECT * FROM Crime WHERE id =(?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __crimeTypeConverters.fromUUID(id);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"Crime"}, false, new Callable<Crime>() {
      @Override
      public Crime call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfIsSolved = CursorUtil.getColumnIndexOrThrow(_cursor, "isSolved");
          final Crime _result;
          if(_cursor.moveToFirst()) {
            final UUID _tmpId;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfId);
            }
            _tmpId = __crimeTypeConverters.toUUID(_tmp_1);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final Date _tmpDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __crimeTypeConverters.toDate(_tmp_2);
            final boolean _tmpIsSolved;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsSolved);
            _tmpIsSolved = _tmp_3 != 0;
            _result = new Crime(_tmpId,_tmpTitle,_tmpDate,_tmpIsSolved);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
