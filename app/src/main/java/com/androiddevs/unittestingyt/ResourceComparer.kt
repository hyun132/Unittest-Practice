package com.androiddevs.unittestingyt

import android.content.Context

class ResourceComparer {

    //여기서 context를 사용하기 때문에 androidTest폴더에 테스트 파일 생성해야함.
    //context는 android 요소이기 때문.
    fun isEqual(context: Context, resId:Int, string :String):Boolean{
       return context.getString(resId) == string
    }

}