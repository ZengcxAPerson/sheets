/*
 *  Copyright (C) 2020. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.maxkeppeler.sheets.core.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.Px
import androidx.annotation.RestrictTo
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import java.text.SimpleDateFormat
import java.util.*

/**
 * Taken from android ktx library [androidx.core.graphics.drawable|.
 *
 * Return a [Bitmap] representation of this [Drawable].
 *
 * If this instance is a [BitmapDrawable] and the [width], [height], and [config] match, the
 * underlying [Bitmap] instance will be returned directly. If any of those three properties differ
 * then a new [Bitmap] is created. For all other [Drawable] types, a new [Bitmap] is created.
 *
 * @param width Width of the desired bitmap. Defaults to [Drawable.getIntrinsicWidth].
 * @param height Height of the desired bitmap. Defaults to [Drawable.getIntrinsicHeight].
 * @param config Bitmap config of the desired bitmap. Null attempts to use the native config, if
 * any. Defaults to [Config.ARGB_8888] otherwise.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun Drawable.toBitmap(
    @Px width: Int = intrinsicWidth,
    @Px height: Int = intrinsicHeight,
    config: Config? = null
): Bitmap {
    if (this is BitmapDrawable) {
        if (config == null || bitmap.config == config) {
            // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
            // involves allocation and two jumps into native code so we perform the check ourselves.
            if (width == intrinsicWidth && height == intrinsicHeight) {
                return bitmap
            }
            return Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
    }

    val (oldLeft, oldTop, oldRight, oldBottom) = bounds

    val bitmap = Bitmap.createBitmap(width, height, config ?: Config.ARGB_8888)
    setBounds(0, 0, width, height)
    draw(Canvas(bitmap))

    setBounds(oldLeft, oldTop, oldRight, oldBottom)
    return bitmap
}