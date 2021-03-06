package com.luxsoft.impapx



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import grails.plugin.springsecurity.annotation.Secured

@Secured(["hasAnyRole('COMRAS','TESORERIA')"])
@Transactional(readOnly = true)
class FacturaDeGastosController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def facturaDeGastosService

    def index(Integer max) {
        params.max = Math.min(max ?: 40, 100)
        params.sort=params.sort?:'lastUpdated'
        params.order='desc'
        respond FacturaDeGastos.list(params), model:[facturaDeGastosInstanceCount: FacturaDeGastos.count()]
    }

    def show(FacturaDeGastos facturaDeGastosInstance) {
        respond facturaDeGastosInstance
    }

    def create() {
        respond new FacturaDeGastos(fecha:new Date(),vencimiento:new Date()+30)
    }

    @Transactional
    def save(FacturaDeGastos facturaDeGastosInstance) {
        if (facturaDeGastosInstance == null) {
            notFound()
            return
        }
        if (facturaDeGastosInstance.hasErrors()) {
            respond facturaDeGastosInstance.errors, view:'create'
            return
        }
        facturaDeGastosInstance.save flush:true
        flash.message = message(code: 'default.created.message', args: [message(code: 'facturaDeGastos.label', default: 'FacturaDeGastos'), facturaDeGastosInstance.id])
        redirect facturaDeGastosInstance
    }

    def edit(FacturaDeGastos facturaDeGastosInstance) {
        respond facturaDeGastosInstance
    }

    @Transactional
    def update(FacturaDeGastos facturaDeGastosInstance) {
        if (facturaDeGastosInstance == null) {
            notFound()
            return
        }

        if (facturaDeGastosInstance.hasErrors()) {
            respond facturaDeGastosInstance.errors, view:'edit'
            return
        }

        facturaDeGastosInstance.save flush:true
        flash.message = message(code: 'default.updated.message', args: [message(code: 'FacturaDeGastos.label', default: 'FacturaDeGastos'), facturaDeGastosInstance.id])
        redirect facturaDeGastosInstance
    }

    @Transactional
    def delete(FacturaDeGastos facturaDeGastosInstance) {

        if (facturaDeGastosInstance == null) {
            notFound()
            return
        }

        facturaDeGastosInstance.delete flush:true
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'FacturaDeGastos.label', default: 'FacturaDeGastos'), facturaDeGastosInstance.id])
        redirect action:"index", method:"GET"
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'facturaDeGastos.label', default: 'FacturaDeGastos'), params.id])
        redirect action: "index", method: "GET"
    }

    @Transactional
    def agregarPartida(ConceptoDeGastoCommand command){
        //log.info 'Concepto: '+command
        def concepto=command.toGasto()
        def fac=facturaDeGastosService.agregarPartida(command)
        render view:'edit', model: [facturaDeGastosInstance: fac]
    }

    @Transactional
    def eliminarConceptos(){
        log.debug 'Eliminando conceptos de gasto: '+params
        def data=[:]
        def fac = FacturaDeGastos.findById(params.facturaId,[fetch:[conceptos:'eager']])
        JSONArray jsonArray=JSON.parse(params.partidas);
        try {
            facturaDeGastosService.eliminarConceptos(fac,jsonArray)
            data.res='CONCEPTOS_ELIMINADOS'
        }
        catch (RuntimeException e) {
            
            log.error 'Error eliminando conceptos: '+ExceptionUtils.getRootCauseMessage(e)
            data.res="ERROR"
            data.error=ExceptionUtils.getRootCauseMessage(e)
        }
        render data as JSON
    }

    def consultarGasto(ConceptoDeGasto concepto){
        //println 'Consultando gasto:'+params
        render(template:"conceptoShowForm",bean:concepto)
        
    }
}

import com.luxsoft.impapx.FacturaDeGastos
import com.luxsoft.impapx.contabilidad.CuentaContable
import com.luxsoft.impapx.cxp.ConceptoDeGasto
import groovy.transform.ToString


@ToString(includeNames=true,includePackage=false)
class ConceptoDeGastoCommand{

    Long factura
    CuentaContable concepto 
    String tipo='GASTOS'
    String descripcion
    BigDecimal importe=0
    BigDecimal impuestoTasa=16
    BigDecimal retensionTasa=0
    BigDecimal retensionIsrTasa=0
    BigDecimal descuento=0
    BigDecimal rembolso=0
    Date fechaRembolso
    BigDecimal otros=0
    String comentarioOtros

    static constraints={
        importFrom ConceptoDeGasto
    }

    ConceptoDeGasto toGasto(){
        ConceptoDeGasto gasto=new ConceptoDeGasto()
        gasto.properties=properties
        //gasto.ietu=0.0
        return gasto
    }
}
