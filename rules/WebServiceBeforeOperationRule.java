import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import connector.common.JsonUtil;
import connector.common.Util;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;

// WebServiceBeforeOperationRule - Intercepts and dynamically modifies request bodies
Map body = requestEndPoint.getBody();
log.info("Rule - Modify Body: body = " + body);
String jsonBody = (String) body.get("jsonBody");
String operationType = requestEndPoint.getOperationType();
log.info("Rule - Modify Body: operationType = " + operationType);
log.info("Rule - Modify Body: running");

try {
  Map jsonMap = JsonUtil.toMap(jsonBody);
  if (jsonMap != null) {
    if ("Create Account".equalsIgnoreCase(operationType)) {
      log.info("Rule - Modify Body: plan " + operationType + " " + provisioningPlan);
      log.info("Plan Arguments : " + provisioningPlan.getArguments());
      for (ProvisioningPlan.AttributeRequest attr :
        provisioningPlan.getAccountRequests().get(0).getAttributeRequests()) {
        log.info("Attribute Name : " + attr.getName() + " | Value : " + attr.getValue());
      }
      // Add in any other missing fields that are required
      if (!jsonMap.containsKey("groups")) {
        Set groups = new LinkedHashSet();
        groups.add("User"); // User is the default group for new accounts
        jsonMap.put("groups", new ArrayList(groups));
      }
    } else if ("Add Entitlement".equalsIgnoreCase(operationType) || "Remove Entitlement".equalsIgnoreCase(operationType)) {
      log.info("Rule - Modify Body: plan " + operationType + " " + provisioningPlan);
      String nativeIdentity = provisioningPlan.getAccountRequests().get(0).getNativeIdentity();
      Object groupsObj = "";
      String attrOperation = null;
      if (provisioningPlan != null) {
        log.info("Rule - Modify Body: plan is not null");
        for (AccountRequest accReq: Util.iterate(provisioningPlan.getAccountRequests())) {
          log.info("Rule - Modify Body: iterating over account requests");
          for (ProvisioningPlan.AttributeRequest attReq: Util.iterate(accReq.getAttributeRequests())) {
            log.info("Rule - Modify Body: iterating over attribute requests");
            String attrName = attReq.getName();
            log.info("Rule - Modify Body: attrName = " + attrName + ", value = " + attReq.getValue());

            if (attrName != null && "groups".equalsIgnoreCase(attrName)) {
              groupsObj = attReq.getValue();
              attrOperation = attReq.getOperation().toString();
              log.info("Rule - Modify Body: groupsObj = " + groupsObj + ", operation = " + attrOperation);
            }
          }
        }
      } else {
        log.info("Rule - Modify Body: plan is null");
      }
      
      Set groups = new LinkedHashSet();
      String baseUrl = application.getAttributeValue("genericWebServiceBaseUrl");
      Map headers = new HashMap();
      headers.put("Content-Type", "application/json");
      List allowedStatuses = new ArrayList();
      allowedStatuses.add("2**");
      
      // Secondary API Call to sync live state
      String response = restClient.executeGet(
          baseUrl + "/api/users?userId=" + nativeIdentity, headers, allowedStatuses);
      Map user = (Map) ((Map) JsonUtil.toMap(response)).get("users");
      if (user != null && user.get("groups") != null) {
        groups.addAll((List) user.get("groups"));
      }
      log.info("Rule - Modify Body: current groups from target = " + groups);
      if ("Add".equals(attrOperation)) {
        groups.add((String) groupsObj);
      } else if ("Remove".equals(attrOperation)) {
        groups.remove((String) groupsObj);
      }
      
      // Business rule: Manager or Super requires User. If User is removed while Manager/Super still exist, add it back.
      if (groups.contains("Manager") || groups.contains("Super")) {
        groups.add("User");
      }
      log.info("Rule - Modify Body: final groups = " + groups);
      if (!groups.isEmpty()) {
        jsonMap.put("groups", new ArrayList(groups));
      }
    }
    String finalBody = JsonUtil.render(jsonMap);
    body.put("jsonBody", finalBody);
    requestEndPoint.setBody(body);
  }
} catch (Exception ex) {
  log.error("Rule - Modify Body: " + ex);
}

return requestEndPoint;
