package com.evastos.bux.ui.util.exception

import com.evastos.bux.data.exception.api.ApiException
import com.evastos.bux.data.exception.rtf.RtfException

interface ExceptionMessageProviders {

    interface Api : ExceptionMessageProvider<ApiException>

    interface Rtf : ExceptionMessageProvider<RtfException>
}
