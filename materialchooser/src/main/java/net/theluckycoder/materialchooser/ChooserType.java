package net.theluckycoder.materialchooser;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({Chooser.FILE_CHOOSER, Chooser.FOLDER_CHOOSER})
public @interface ChooserType {}
