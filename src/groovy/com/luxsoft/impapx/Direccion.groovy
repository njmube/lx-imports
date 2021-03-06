package com.luxsoft.impapx

import grails.validation.Validateable;


@Validateable
class Direccion {
	
	String calle
	String numeroInterior
	String numeroExterior
	String colonia
	String municipio
	String codigoPostal
	String estado
	String pais

	
    static constraints = {
		calle(size:1..200)
		numeroInterior(size:1..50,nullable:true)
		numeroExterior(size:1..50,nullable:true)
		colonia(nullable:true)
		municipio(nullable:true)
		codigoPostal()
		estado()
		pais(size:1..100)
    }
	
	String toString(){
		return "${calle} ${numeroInterior?:''} ${numeroExterior?:''} ${colonia}"
	}
}
