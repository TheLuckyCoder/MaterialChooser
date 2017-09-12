package net.theluckycoder.filechooser;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({Chooser.FILE_CHOOSER, Chooser.FOLDER_CHOOSER})
public @interface ChooserType {}
