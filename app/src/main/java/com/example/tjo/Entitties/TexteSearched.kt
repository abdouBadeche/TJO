package com.example.tjo.Entitties

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName= "texte_searched_table")
data class TexteSearched(
    @PrimaryKey()
    var NumSGG: String ,
    var NumJO: String,
    var Type_Texte_FR: String,
    var Type_Texte_AR: String,
    var Page_FR: String,
    var Page_AR: String,
    var Date_publication_FR: String,
    var Date_Publication_AR: String,
    var Date_Signature_FR: String,
    var Date_Signature_AR: String,
    var Sommaire_FR: String,
    var Sommaire_AR: String,
    var ATexte: String,
    var FTexte: String,
    var Organe_FR: String,
    var Organe_AR: String,
    var AnneeJO: String,
    var F_PDFFileName: String,
    var A_PDFFileName: String,
){

}