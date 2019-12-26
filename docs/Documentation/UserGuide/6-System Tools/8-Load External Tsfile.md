<!--

    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

-->

# Chapter 6: System Tools
# Load External Tsfile Tool

# Introduction
The load external tsfile tool allows users to load tsfiles, delete a tsfile, or move a tsfile to target directory from the running Apache IoTDB instance.

# Usage
The user sends specified commands to the Apache IoTDB system through the Cli tool or JDBC to use the tool.

## load tsfiles
The command to load tsfiles is `load <path/dir> [true/false] [storage group level]`.

This command has two usages:
1. Load a single tsfile by specifying a file path (absolute path). 
The second parameter indicates the path of the tsfile to be loaded and the name of the tsfile needs to conform to the tsfile naming convention, that is, `{systemTime}-{versionNum}-{mergeNum} .tsfile`. The third and fourth parameters are optional. When the metadata corresponding to the timeseries in the tsfile to be loaded does not exist, you can choose whether to create the schema automatically. If the third parameter is true, the schema will be created automatically. If the thrid parameter is false, the schema will not be created. By default, the schema will be created. When the storage group corresponding to the tsfile does not exist, the user can set the level of storage group through the fourth parameter. By default, it'll use the storage group level which is set in `iotdb-engine.properties`.
If the `.resource` file corresponding to the file exists, it will be loaded into the data directory and engine of the Apache IoTDB. Otherwise, the corresponding `.resource` file will be regenerated from the tsfile file.
Examples:
    * `load /Users/Desktop/data/1575028885956-101-0.tsfile`
    * `load /Users/Desktop/data/1575028885956-101-0.tsfile false`
    * `load /Users/Desktop/data/1575028885956-101-0.tsfile true`
    * `load /Users/Desktop/data/1575028885956-101-0.tsfile true 2`

2. Load a batch of files by specifying a folder path (absolute path). 
The second parameter indicates the path of the tsfile to be loaded and the name of the tsfiles need to conform to the tsfile naming convention, that is, `{systemTime}-{versionNum}-{mergeNum} .tsfile`. The third and fourth parameters are optional. When the metadata corresponding to the time series in the tsfile to be loaded does not exist, you can choose whether to create the schema automatically. If the third parameter is true, the schema will be created automatically. If the third parameter is false, the schema will not be created. By default, the schema will be created. When the storage group corresponding to tsfile does not exist, the user can set the level of storage group through the fourth parameter. By default, it'll use the storage group level which is set in `iotdb-engine.properties`.
If the `.resource` file corresponding to the file  exists, they will be loaded into the data directory and engine of the Apache IoTDB. Otherwise, the corresponding` .resource` files will be regenerated from the tsfile sfile.
Examples:
    * `load /Users/Desktop/data`
    * `load /Users/Desktop/data false`
    * `load /Users/Desktop/data true`
    * `load /Users/Desktop/data true 2`