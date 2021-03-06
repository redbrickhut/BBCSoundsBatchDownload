package org.menzies.model.service.tagging;

import java.io.File;

public interface AudioTagger {

    /*
    * Returns false if file is already set. Must be called before
    * addTag,
    */
    boolean setFile(File file);

    boolean addTag(String ID, String value);

    boolean clearTags();

    boolean commit();
}
