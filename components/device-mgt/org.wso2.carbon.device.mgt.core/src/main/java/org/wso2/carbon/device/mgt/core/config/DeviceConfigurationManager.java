/*
 * Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.mgt.core.config;

import org.w3c.dom.Document;
import org.wso2.carbon.device.mgt.common.DeviceManagementConstants;
import org.wso2.carbon.device.mgt.common.DeviceManagementException;
import org.wso2.carbon.device.mgt.core.config.datasource.DataSourceConfig;
import org.wso2.carbon.device.mgt.core.util.DeviceManagerUtil;
import org.wso2.carbon.utils.CarbonUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Class responsible for the cdm manager configuration initialization
 */
public class DeviceConfigurationManager {

    private DeviceManagementConfig currentDeviceConfig;
    private static DeviceConfigurationManager deviceConfigManager;

    private static final String deviceMgtConfigXMLPath = CarbonUtils.getCarbonConfigDirPath() + File.separator  +
            DeviceManagementConstants.DataSourceProperties.DEVICE_CONFIG_XML_NAME;

    public static DeviceConfigurationManager getInstance() {
        if (deviceConfigManager == null) {
            synchronized (DeviceConfigurationManager.class) {
                if (deviceConfigManager == null) {
                    deviceConfigManager = new DeviceConfigurationManager();
                }
            }
        }
        return deviceConfigManager;
    }

    public synchronized void initConfig() throws DeviceManagementException {

        //catch generic exception.if any exception occurs wrap and throw DeviceManagementException
        try {
            File deviceMgtConfig = new File(deviceMgtConfigXMLPath);
            Document doc = DeviceManagerUtil.convertToDocument(deviceMgtConfig);

            /* Un-marshaling Device Management configuration */
            JAXBContext cdmContext = JAXBContext.newInstance(DeviceManagementConfig.class);
            Unmarshaller unmarshaller = cdmContext.createUnmarshaller();
            this.currentDeviceConfig = (DeviceManagementConfig) unmarshaller.unmarshal(doc);
        } catch (Exception e) {
            throw new DeviceManagementException("Error occurred while initializing RSS config", e);
        }
    }

    public DeviceManagementConfig getDeviceManagementConfig() {
        return currentDeviceConfig;
    }

}
