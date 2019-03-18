package demo.singleton

/**
 * Created by s1y on 2019/3/15.
 */
 class SingleTonHanger {
    private  val  singleTonHanger:SingleTonHanger ?= null

    fun getInstance() : SingleTonHanger? {
        if (singleTonHanger == null){
            synchronized(this){
                if (singleTonHanger == null){
                    return SingleTonHanger()
                }
            }
        }
        return singleTonHanger
    }
}