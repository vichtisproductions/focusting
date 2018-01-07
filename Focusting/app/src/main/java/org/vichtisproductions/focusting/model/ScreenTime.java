package org.vichtisproductions.focusting.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by chris.teli on 3/20/2016.
 */
@RealmClass
public class ScreenTime extends RealmObject {
    @PrimaryKey
    private String id;
    private String lock_time_stamp;
    private String unlock_time_stamp;
    private Date observed_date;

    public String getLock_time_stamp() {
        return lock_time_stamp;
    }

    public void setLock_time_stamp(String lock_time_stamp) {
        this.lock_time_stamp = lock_time_stamp;
    }

    public String getUnlock_time_stamp() {
        return unlock_time_stamp;
    }

    public void setUnlock_time_stamp(String unlock_time_stamp) {
        this.unlock_time_stamp = unlock_time_stamp;
    }

    public Date getObserved_date() {
        return observed_date;
    }

    public void setObserved_date(Date observed_date) {
        this.observed_date = observed_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
