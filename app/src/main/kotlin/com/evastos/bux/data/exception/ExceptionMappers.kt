package com.evastos.bux.data.exception

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.exception.rtf.RtfException

interface ExceptionMappers {

    interface Api : ExceptionMapper<ApiException>

    interface Rtf : ExceptionMapper<RtfException>
}
