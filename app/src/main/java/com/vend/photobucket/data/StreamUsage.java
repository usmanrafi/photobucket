package com.vend.photobucket.data;

import android.os.Build;

import com.vend.photobucket.model.Image;

import java.util.ArrayList;

import io.realm.Realm;

public class StreamUsage {

    public static void convertImageTitlesToUppercase(Realm realm, final ArrayList<Image> images) {
        realm.executeTransaction(it -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                images.stream()
                        .map(temp -> {
                            temp.convertTitleToUpperCase();
                            return temp;
                        })
                        .forEach(it::copyToRealmOrUpdate);
            }
        });
    }
}
