package com.singtel.dashboard;

import org.apache.log4j.Logger;

import com.singtel.dashboard.namespaces.csrDashboard;
import com.singtel.dashboard.namespaces.dashboard;
import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.ConfIdentityRef;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfUInt32;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuList;

public class DashboardUpdater {
	private static Logger LOGGER = Logger.getLogger(DashboardUpdater.class);

	// **********************************Methods to Update

	// Method to update Dashboard model
	public void updateDashboard(NavuContainer ncsRoot, ConfKey serviceName,
			String leafToBeUpdated) {
		// If the brn doesnt exist make a new record
		NavuContainer customersCont;
		NavuContainer servicesCont;
		NavuContainer serviceInstance;
		try {
			serviceInstance = ncsRoot.getParent().container(nfv.uri)
					.container(nfv._megapop_).list(nfv._l2nid_mpls_csr_)
					.elem(serviceName);
			customersCont = ncsRoot.getParent().container(dashboard.uri)
					.container(dashboard._customers_);

			servicesCont = customersCont.list(dashboard._customer_)
					.elem(getBrn(serviceInstance))
					.container(dashboard._services_);

			// Set values to all the variables in dashboard model

			String toBeUpdated = serviceInstance.leaf(leafToBeUpdated)
					.valueAsString();
			if (leafToBeUpdated.compareTo("brn") == 0) {
				return;
			}
			if (leafToBeUpdated.compareTo("site-code") == 0) {
				servicesCont.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance)).leaf("dc")
						.set(toBeUpdated);
			} else if (leafToBeUpdated.compareTo("speed") == 0) {
				servicesCont
						.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance))
						.leaf(leafToBeUpdated)
						.set(new ConfUInt32(Integer.parseInt(serviceInstance
								.leaf(nfv._speed_).valueAsString())));
			} else if (leafToBeUpdated.compareTo("access-service-type") == 0) {
				servicesCont
						.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance))
						.leaf(dashboard._network_)
						.set(serviceInstance.leaf("access-service-type")
								.valueAsString());
			}

			else if (leafToBeUpdated.compareTo("service-type") == 0) {
				servicesCont
						.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance))
						.leaf(dashboard._service_type_)
						.set(new ConfIdentityRef(csrDashboard.uri,
								csrDashboard._vcpe_));

			} else {
				servicesCont.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance))
						.leaf(leafToBeUpdated).set(toBeUpdated);
			}

		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	// Method to update CsrDashboard Model

	public void updateCsrDashboard(NavuContainer ncsRoot, ConfKey serviceName,
			String csrDbOrDb, String leafToBeUpdated) {
		NavuContainer customersCont;
		NavuContainer servicesCont;
		NavuContainer serviceInstance;
		try {
			serviceInstance = ncsRoot.getParent().container(nfv.uri)
					.container(nfv._megapop_).list(nfv._l2nid_mpls_csr_)
					.elem(serviceName);

			// get the customers container
			customersCont = ncsRoot.getParent().container(dashboard.uri)
					.container(dashboard._customers_);
			// get the services container

			if (!customersCont.list(dashboard._customer_).containsNode(
					getBrn(serviceInstance))) {
				customersCont.list(dashboard._customer_).sharedCreate(
						getBrn(serviceInstance));
			}
			// If the service does not exist, make a new service
			servicesCont = customersCont.list(dashboard._customer_)
					.elem(getBrn(serviceInstance))
					.container(dashboard._services_);

			if (!servicesCont.list(dashboard._service_).containsNode(
					getNfvVasReference(serviceInstance))) {
				servicesCont.list(dashboard._service_).sharedCreate(
						getNfvVasReference(serviceInstance));
				// Initialise all the variables in the model
				initialiseServiceList(servicesCont, serviceInstance);
			}

			// Set values for all the variables in csr dashboard
			if (csrDbOrDb.compareTo("csr") == 0) {
				String toBeUpdated = "";
				if (leafToBeUpdated.compareTo(nfv._sid_) == 0) {
					toBeUpdated = serviceInstance.leaf(nfv._sid_)
							.valueAsString();
				} else if (leafToBeUpdated.compareTo(nfv._service_name_) == 0) {

					toBeUpdated = serviceInstance.leaf(nfv._service_name_)
							.valueAsString();
				} else {
					toBeUpdated = serviceInstance.container(nfv._service_info_)
							.leaf(leafToBeUpdated).valueAsString();
				}

				servicesCont.list(dashboard._service_)
						.elem(getNfvVasReference(serviceInstance))
						.container("dbcsr", csrDashboard._vcpe_)
						.leaf(leafToBeUpdated).set(toBeUpdated);
			} else {
				updateDashboard(ncsRoot, serviceName, leafToBeUpdated);
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	// **********************************Method to Delete Dashboard
	// Element**********************************

	public void deleteDashboardEntry(NavuContainer ncsRoot, ConfKey serviceName) {

		String brnToBeDeleted = "";
		String nfvVasRefToBeDeleted = "";
		NavuList customerList = null;
		NavuList serviceList = null;
		try {
			// get customer list
			customerList = ncsRoot.getParent().container(dashboard.uri)
					.container(dashboard._customers_)
					.list(dashboard._customer_);
			// iterate through every customer to get the brn which corresponds
			// to the service that is deleted
			for (NavuContainer customer : customerList.elements()) {
				serviceList = customer.container(dashboard._services_).list(
						dashboard._service_);
				for (NavuContainer service : serviceList.elements()) {
					String serviceNameFromYang = "{"
							+ service.container("dbcsr", csrDashboard._vcpe_)
									.leaf(csrDashboard._service_name_)
									.valueAsString() + "}";
					if (serviceNameFromYang.compareTo(serviceName.toString()) == 0) {
						brnToBeDeleted = customer.leaf(dashboard._brn_)
								.valueAsString();
						nfvVasRefToBeDeleted = service.leaf(
								dashboard._nfv_vas_reference_).valueAsString();
						customerList.elem(brnToBeDeleted)
								.container(dashboard._services_)
								.list(dashboard._service_)
								.delete(nfvVasRefToBeDeleted);
						if (customerList.elem(brnToBeDeleted)
								.container(dashboard._services_)
								.list(dashboard._service_).isEmpty()) {
							customerList.delete(brnToBeDeleted);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	// **************************** Helper methods to retrieve nfv_vas_ref and
	// brn********************************************
	public String getNfvVasReference(NavuContainer serviceInstance) {
		try {

			return serviceInstance.leaf(nfv._nfv_vas_reference_)
					.valueAsString();
		} catch (NavuException e) {
			LOGGER.error(" ", e);
		}
		return " ";
	}

	public String getBrn(NavuContainer serviceInstance) {
		try {
			if (serviceInstance.leaf(nfv._brn_).valueAsString() == null) {
				return " ";

			} else {
				return serviceInstance.leaf(nfv._brn_).valueAsString();
			}

		} catch (NavuException e) {
			LOGGER.error("", e);
		}
		return " ";
	}

	// Helper method to initialise dashboard
	private void initialiseServiceList(NavuContainer servicesCont,
			NavuContainer serviceInstance) {

		// Dashboard
		try {
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._ban_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._deploy_status_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._operational_status_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._brand_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._circuit_reference_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._dc_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._network_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._nfv_access_setup_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._nfv_setup_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._nfv_vas_scheme_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._product_code_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._speed_).set(new ConfUInt32(0));
			servicesCont
					.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.leaf(dashboard._service_type_)
					.set(new ConfIdentityRef(csrDashboard.uri,
							csrDashboard._vcpe_));

			// CsrDashboard
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.container("dbcsr", csrDashboard._vcpe_)
					.leaf(csrDashboard._network_service_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.container("dbcsr", csrDashboard._vcpe_)
					.leaf(csrDashboard._service_name_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.container("dbcsr", csrDashboard._vcpe_)
					.leaf(csrDashboard._nickname_).set("");
			servicesCont.list(dashboard._service_)
					.elem(getNfvVasReference(serviceInstance))
					.container("dbcsr", csrDashboard._vcpe_)
					.leaf(csrDashboard._sid_).set("");

		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	public void deleteDashboardLeaf(NavuContainer ncsRoot, ConfKey serviceName,
			String csrDbOrDb, String leafToBeUpdated) {
		NavuList customerList = null;
		NavuList serviceList = null;
		String brnToBeDeleted = "";
		String nfvVasRefToBeDeleted = "";

		try {
			// get customer list
			customerList = ncsRoot.getParent().container(dashboard.uri)
					.container(dashboard._customers_)
					.list(dashboard._customer_);
			// iterate through every customer to get the brn which corresponds
			// to the service that is deleted
			for (NavuContainer customer : customerList.elements()) {
				serviceList = customer.container(dashboard._services_).list(
						dashboard._service_);
				for (NavuContainer service : serviceList.elements()) {
					String serviceNameFromYang = "{"
							+ service.container("dbcsr", csrDashboard._vcpe_)
									.leaf(csrDashboard._service_name_)
									.valueAsString() + "}";
					if (serviceNameFromYang.compareTo(serviceName.toString()) == 0) {
						if (csrDbOrDb.compareTo("csr") == 0) {
							service.container("dbcsr", csrDashboard._vcpe_)
									.leaf(leafToBeUpdated).set("");
						} else {

							if (leafToBeUpdated.compareTo("speed") == 0) {
								service.leaf(leafToBeUpdated).set(
										new ConfUInt32(0));
								return;
							}
							service.leaf(leafToBeUpdated).set("");
						}

						return;
					}
				}
			}

		} catch (Exception e) {
			LOGGER.info("", e);
		}
	}

}
