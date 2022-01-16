/*
 * Copyright (c) 2016-2026 Jumin Rubin
 * LinkedIn: https://www.linkedin.com/in/juminrubin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jrtech.engines.rules.model;

import java.util.List;

public class SimplifiedBusinessProcessDefinition extends BusinessProcessDefinition {

    private static final long serialVersionUID = 4443647563574885207L;

    private String partySide;

    private String extendedStpProcessing;

    private List<RuleCondition> complianceCondition;
    private List<RuleCondition> destinationRoutingCondition;
    private List<RuleCondition> manualApprovalCondition;
    private List<RuleCondition> manualCorrectionCondition;
    private List<RuleCondition> manualDuplicateHandlingCondition;
    private List<RuleCondition> manualRepairCondition;
    private List<RuleCondition> orderCondition;
    private List<RuleCondition> printingCondition;

    private String targetPrintLink;

    private String destinationRouting;

    public SimplifiedBusinessProcessDefinition() {
        super();

        super.setObjectType(getClass().getSimpleName());
    }

    public String getPartySide() {
        return partySide;
    }

    public void setPartySide(String partySide) {
        this.partySide = partySide;
    }

    public List<RuleCondition> getComplianceCondition() {
        return complianceCondition;
    }

    public void setComplianceCondition(List<RuleCondition> complianceCondition) {
        this.complianceCondition = complianceCondition;
    }

    public List<RuleCondition> getManualApprovalCondition() {
        return manualApprovalCondition;
    }

    public void setManualApprovalCondition(List<RuleCondition> manualApprovalCondition) {
        this.manualApprovalCondition = manualApprovalCondition;
    }

    public List<RuleCondition> getManualCorrectionCondition() {
        return manualCorrectionCondition;
    }

    public void setManualCorrectionCondition(List<RuleCondition> manualCorrectionCondition) {
        this.manualCorrectionCondition = manualCorrectionCondition;
    }

    public List<RuleCondition> getManualRepairCondition() {
        return manualRepairCondition;
    }

    public void setManualRepairCondition(List<RuleCondition> manualRepairCondition) {
        this.manualRepairCondition = manualRepairCondition;
    }

    public List<RuleCondition> getManualDuplicateHandlingCondition() {
        return manualDuplicateHandlingCondition;
    }

    public void setManualDuplicateHandlingCondition(List<RuleCondition> manualDuplicateHandlingCondition) {
        this.manualDuplicateHandlingCondition = manualDuplicateHandlingCondition;
    }

    public List<RuleCondition> getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(List<RuleCondition> orderCondition) {
        this.orderCondition = orderCondition;
    }

    public List<RuleCondition> getPrintingCondition() {
        return printingCondition;
    }

    public void setPrintingCondition(List<RuleCondition> printingCondition) {
        this.printingCondition = printingCondition;
    }

    public List<RuleCondition> getDestinationRoutingCondition() {
        return destinationRoutingCondition;
    }

    public void setDestinationRoutingCondition(List<RuleCondition> destinationRoutingCondition) {
        this.destinationRoutingCondition = destinationRoutingCondition;
    }

    public String getExtendedStpProcessing() {
        return extendedStpProcessing;
    }

    public void setExtendedStpProcessing(String extendedStpProcessing) {
        this.extendedStpProcessing = extendedStpProcessing;
    }

    public String getTargetPrintLink() {
        return targetPrintLink;
    }

    public void setTargetPrintLink(String targetPrintLink) {
        this.targetPrintLink = targetPrintLink;
    }

    public String getDestinationRouting() {
        return destinationRouting;
    }

    public void setDestinationRouting(String destinationRouting) {
        this.destinationRouting = destinationRouting;
    }

}
