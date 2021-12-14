package com.fenoreste.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fenoreste.dto.BackendOperationResultDTO;
import com.fenoreste.dto.Bank;
import com.fenoreste.dto.ThirdPartyProductDTO;
import com.fenoreste.dto.UserDocumentIdDTO;
import com.fenoreste.dto.opaDTO;
import com.fenoreste.entity.Auxiliares;
import com.fenoreste.entity.Colonia;
import com.fenoreste.entity.Estados;
import com.fenoreste.entity.Municipios;
import com.fenoreste.entity.Origenes;
import com.fenoreste.entity.Paises;
import com.fenoreste.entity.Persona;
import com.fenoreste.entity.ProductoTercero;
import com.fenoreste.entity.TiposCuentaBankingly;
import com.fenoreste.util.HerramientasUtil;

import ch.qos.logback.classic.pattern.Util;

@Service
public class TerceroServiceSpring {

	@Autowired
	ITercerosService tercerosService;	
	@Autowired
	IAuxiliaresService auxiliaresService;
	@Autowired
	ITiposCuentaBankinglyService cuentasBankinlyService;
	@Autowired
	IPersonaService personaService;
	@Autowired 
	IColoniasService coloniaSevice;
	@Autowired
	IMunicipiosService municipiosService;
	@Autowired
	IEstadosService estadosService;
	@Autowired
	IPaisesService paisesService;
	@Autowired
	IOrigenesService origenesService;
	
	

	public BackendOperationResultDTO validarProductoTerceros(ThirdPartyProductDTO dtoInput) {
		BackendOperationResultDTO dtoResult = new BackendOperationResultDTO();
		try {
			String backendMessage = "";
			ProductoTercero productosTerceros = new ProductoTercero();

			System.out.println("size:" + dtoInput.getClientBankIdentifiers().size());
			boolean b = false;
			ProductoTercero producto_tercero = null;

			for (int i = 0; i < dtoInput.getClientBankIdentifiers().size(); i++) {
				producto_tercero = tercerosService.findTerceroByCuenta(dtoInput.getThirdPartyProductBankIdentifier());
				if (producto_tercero != null) {
					backendMessage = "Error,producto ya esta registrado...";
					dtoResult.setBackendCode("2");
					dtoResult.setBackendMessage(backendMessage);
					dtoResult.setBackendReference("null");
					dtoResult.setIntegrationProperties("null");
					dtoResult.setIsError(true);
					dtoResult.setTransactionIdenty(productosTerceros.getThirdPartyProductNumber());
				} else {
					productosTerceros.setThirdPartyProductBankIdentifier(dtoInput.getThirdPartyProductBankIdentifier());
					productosTerceros.setClientBankIdentifiers(dtoInput.getClientBankIdentifiers().get(i));
					productosTerceros.setThirdPartyProductNumber(dtoInput.getThirdPartyProductNumber());
					
					productosTerceros.setAlias(dtoInput.getAlias());
					productosTerceros.setCurrencyId(dtoInput.getCurrencyId());
					productosTerceros.setTransactionSubType(dtoInput.getTransactionSubType());
					productosTerceros.setThirdPartyProductType(dtoInput.getThirdPartyProductType());
					productosTerceros.setProductType(dtoInput.getProductType());
					productosTerceros.setOwnerName(dtoInput.getOwnerName());
					productosTerceros.setOwnerCountryId(dtoInput.getOwnerCountryId());
					productosTerceros.setOwnerEmail(dtoInput.getOwnerEmail());
					productosTerceros.setOwnerCity(dtoInput.getOwnerCity());
					productosTerceros.setOwnerAddress(dtoInput.getOwnerAddress());
					productosTerceros.setOwnerDocumentId_integrationProperties(
							dtoInput.getOwnerDocumentId().getIntegrationProperties());
					productosTerceros.setOwnerDocumentId_documentNumber(
							String.valueOf(dtoInput.getOwnerDocumentId().getDocumentNumber()));
					productosTerceros.setOwnerDocumentId_documentType(
							String.valueOf(dtoInput.getOwnerDocumentId().getDocumentType()));
					productosTerceros.setOwnerPhoneNumber(dtoInput.getOwnerPhoneNumber());
					productosTerceros.setBank_bankId(dtoInput.getBank().getBankId());
					productosTerceros.setBank_countryId(dtoInput.getBank().getCountryId());
					productosTerceros.setBank_description(dtoInput.getBank().getDescription());
					productosTerceros.setBank_headQuartersAddress(dtoInput.getBank().getHeadQuartersAddress());
					productosTerceros.setBank_routingCode(dtoInput.getBank().getRoutingCode());
					productosTerceros.setCorrespondentBank_bankId(dtoInput.getCorrespondentBank().getBankId());
					productosTerceros.setCorrespondentBank_countryId(dtoInput.getCorrespondentBank().getCountryId());
					productosTerceros
							.setCorrespondentBank_description(dtoInput.getCorrespondentBank().getDescription());
					productosTerceros.setCorrespondentBank_headQuartersAddress(
							dtoInput.getCorrespondentBank().getHeadQuartersAddress());
					productosTerceros
							.setCorrespondentBank_routingCode(dtoInput.getCorrespondentBank().getRoutingCode());
					productosTerceros.setUserDocumentId_documentNumber(
							String.valueOf(dtoInput.getUserDocumentId().getDocumentNumber()));
					productosTerceros.setUserDocumentId_documentType(
							String.valueOf(dtoInput.getUserDocumentId().getDocumentType()));
					productosTerceros.setUserDocumentId_integrationProperties(
							dtoInput.getUserDocumentId().getIntegrationProperties());
					
					
					dtoResult.setBackendCode("1");
					dtoResult.setBackendMessage(backendMessage);
					dtoResult.setBackendReference("null");
					dtoResult.setIntegrationProperties("null");
					dtoResult.setIsError(false);
					dtoResult.setTransactionIdenty(productosTerceros.getThirdPartyProductNumber());
					tercerosService.guardar(productosTerceros);

				}
				
			}
		} catch (Exception e) {
			System.out.println("Error en registrar tercero : " + e.getMessage());
		}
		return dtoResult;
	}
	
	  public ThirdPartyProductDTO cosultaProductosTerceros(String productNumber, Integer productTypeId, UserDocumentIdDTO documento, Integer thirdPartyProductType) {
        opaDTO opa = new HerramientasUtil().opa(productNumber);
        ThirdPartyProductDTO dto = new ThirdPartyProductDTO();
        try {
            Auxiliares a = auxiliaresService.AuxiliarByOpa(opa.getIdorigenp(),opa.getIdproducto(),opa.getIdauxiliar());
            //Busco el tipo de producto
            TiposCuentaBankingly tipo_cuenta = cuentasBankinlyService.findTipoCuentaById(opa.getIdproducto());
            
            if (a != null) {
            	if(tipo_cuenta.getProductTypeId() == productTypeId) {
            		UserDocumentIdDTO userDocument = new UserDocumentIdDTO();
                    Bank bancoProductoTercero = new Bank();
                    Bank corresponsalBank = new Bank();
                    String ogs = String.format("%06d", a.getIdorigen()) + String.format("%02d", a.getIdgrupo()) + String.format("%06d", a.getIdsocio());
                    ArrayList<String> listaPt = new ArrayList<>();
                    listaPt.add(ogs);
                    dto.setClientBankIdentifiers(listaPt);
                    dto.setThirdPartyProductNumber(String.valueOf(thirdPartyProductType));
                    dto.setThirdPartyProductBankIdentifier(productNumber);
                    dto.setAlias(tipo_cuenta.getDescripcion());
                    dto.setCurrencyId("484");//Identificador de moneda 1 es local
                    dto.setTransactionSubType(2);
                    dto.setThirdPartyProductType(1);

                    TiposCuentaBankingly prod = cuentasBankinlyService.findTipoCuentaById(opa.getIdproducto());
                    dto.setProductType(prod.getProductTypeId());//el tipo de producto

                    
                    Persona p = personaService.findByOGS(a.getIdorigen(),a.getIdgrupo(),a.getIdsocio());
                    dto.setOwnerName(p.getNombre() + " " + p.getAppaterno() + " " + p.getApmaterno());

                    //Otenemos el nombre del pais de la persona
                    Colonia c = coloniaSevice.findColoniaById(p.getIdcolonia());
                    
                    Municipios m =  municipiosService.findMunicipioById(c.getIdmunicipio());
                    Estados e = estadosService.findEstadoById(m.getIdestado());
                    Paises pa = paisesService.findPaisById(e.getIdpais());
                    dto.setOwnerCountryId("484");//Moneda nacional interncional cambia de codigo a 840
                    dto.setOwnerEmail(p.getEmail());
                    dto.setOwnerCity(c.getNombre());
                    dto.setOwnerAddress(c.getNombre() + "," + p.getNumeroext() + "," + p.getNumeroint());
                    //Creamos y llenamos documento para el titular del producto de tercero
                    UserDocumentIdDTO ownerDocumentId = new UserDocumentIdDTO();
                    ownerDocumentId.setDocumentNumber(p.getIdorigen() + p.getIdgrupo() + p.getIdsocio());//Se a solicitado a Bankingly
                    ownerDocumentId.setDocumentType(3);//Se a solicitado a Bankingly
                    dto.setOwnerDocumentId(ownerDocumentId);
                    dto.setOwnerPhoneNumber(p.getCelular());
                    //Llenamos user document Id
                    userDocument.setDocumentNumber(p.getIdorigen() + p.getIdgrupo() + p.getIdsocio());//
                    userDocument.setDocumentType(3);
                    dto.setUserDocumentId(userDocument);
                    //Llenamos el banco de tercero
                    bancoProductoTercero.setBankId(a.getIdorigenp());
                    bancoProductoTercero.setCountryId("484");
                    Origenes o = origenesService.findOrigenById(a.getIdorigenp());
                    bancoProductoTercero.setDescription(o.getNombre());
                    bancoProductoTercero.setRoutingCode(null);
                    bancoProductoTercero.setHeadQuartersAddress(o.getCalle() + "," + o.getNumeroint() + "," + o.getNumeroext());
                    dto.setBank(bancoProductoTercero);
                    dto.setCorrespondentBank(corresponsalBank);
                    }
               }
        } catch (Exception e) {
            System.out.println("Error al buscar tercero :" + e.getMessage());

        } 
        return dto;

    }
    
   


}
