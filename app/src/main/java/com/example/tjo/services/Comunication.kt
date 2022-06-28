package com.example.tjo.services

interface Comunication {
    fun getDate(param : String = "" , type : String = "" , secteur : String = ""  , mot : String = ""  , organe : String = "" , date_debut : Int = 0 , date_fin : Int  = 0 )
}