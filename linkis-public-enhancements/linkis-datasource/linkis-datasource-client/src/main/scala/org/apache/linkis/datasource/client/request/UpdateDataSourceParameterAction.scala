/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.linkis.datasource.client.request

import org.apache.linkis.datasource.client.config.DatasourceClientConfig.DATA_SOURCE_SERVICE_MODULE
import org.apache.linkis.datasource.client.exception.DataSourceClientBuilderException
import org.apache.linkis.httpclient.dws.DWSHttpClient
import org.apache.linkis.httpclient.request.POSTAction

import scala.collection.JavaConverters._
import java.util

class UpdateDataSourceParameterAction extends POSTAction with DataSourceAction {
  override def getRequestPayload: String = DWSHttpClient.jacksonJson.writeValueAsString(getRequestPayloads)

  private var user: String = _
  private var dataSourceId: String = _

  override def setUser(user: String): Unit = this.user = user

  override def getUser: String = this.user

  override def suffixURLs: Array[String] = Array(DATA_SOURCE_SERVICE_MODULE.getValue, "parameter", dataSourceId, "json")
}
object UpdateDataSourceParameterAction {
  def builder(): Builder = new Builder

  class Builder private[UpdateDataSourceParameterAction]() {
    private var user: String = _
    private var dataSourceId: String = _
    private var payload: util.Map[String, Any] = new util.HashMap[String, Any]

    def setUser(user: String): Builder = {
      this.user = user
      this
    }

    def setDataSourceId(dataSourceId: String): Builder = {
      this.dataSourceId = dataSourceId
      this
    }

    def addRequestPayload(key: String, value: Any): Builder = {
      if (value != null) this.payload.put(key, value)
      this
    }

    def addRequestPayloads(map: util.Map[String, Any]): Builder = {
      this.synchronized(this.payload = map)
      this
    }

    def build(): UpdateDataSourceParameterAction = {
      if (dataSourceId == null) throw new DataSourceClientBuilderException("dataSourceId is needed!")
      if(user == null) throw new DataSourceClientBuilderException("user is needed!")

      val action = new UpdateDataSourceParameterAction()
      action.dataSourceId = dataSourceId
      action.user = user
      this.payload.asScala.foreach(k => {
        action.addRequestPayload(k._1, k._2)
      })

      action
    }
  }
}