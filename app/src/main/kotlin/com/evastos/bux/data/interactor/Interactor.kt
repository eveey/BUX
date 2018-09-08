package com.evastos.bux.data.interactor

interface Interactor<Request, Response> {
    fun execute(request: Request): Response
}