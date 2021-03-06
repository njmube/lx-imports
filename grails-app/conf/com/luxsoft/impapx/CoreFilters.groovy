package com.luxsoft.impapx

import com.luxsoft.utils.Periodo
import com.luxsoft.impapx.Empresa
class CoreFilters {

    def filters = {
        
        all(controller:'*', action:'*') {

            before = {
                if(!session.periodo){
                    session.periodo=Periodo.ytd()
                }
                if(!session.ejercicion){
                    session.ejercicio=Periodo.obtenerYear(new Date())
                }
                
                if(!session.mes){
                    session.mes=Periodo.obtenerMes(new Date())
                }
                if(!session.empresa){
                    session.empresa=Empresa.first()
                }
                /*
                if(!session.periodoContable){
                    def today=new Date()
                    session.periodoContable=new PeriodoContable(
                            ejercicio:Periodo.obtenerYear(today),
                            mes:Periodo.obtenerMes(today)+1
                        )
                }
                */
            }
            after = { Map model ->
                
            }
            afterView = { Exception e ->

            }
        }
    }
}
