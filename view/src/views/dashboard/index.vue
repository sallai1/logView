<template>
  <div class="dashboard-container">
    <div>
      <!-- <el-divider content-position="center">为了无法计算的价值</el-divider> -->
      <div class="dashboard-text">
        <div style="font-size: 20px;">
          <label style="color: #4c4c4c;">日志文件名: </label> <label>{{ logInfo.name }}</label>
          <label style="color: #4c4c4c; margin-left: 10px;">大小: </label> <label>{{ logInfo.size }}</label>
        </div>
      </div>
  
      <el-input type="number" style="display: inline-block; width: 100px;" v-model="queryNum"
        placeholder="查询行数"></el-input>
      <el-button style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="queryLog">获取日志</el-button>
      <el-select style="display: inline-block; width: fit-content; margin-left: 10px;" v-model="selectedLogLevel" @change="logLevelChange" placeholder="Select Log Level">
      <el-option
        v-for="level in logLevels"
        :key="level"
        :label="level"
        :value="level"
      ></el-option>
    </el-select>
    </div>
    <div>
      <el-tabs class="logTab" v-model="activeTab">
        <el-tab-pane class="pane" label="日志信息" name="info">
          <pre v-if="logInfo.content" v-html="logInfo.content"></pre>
          <el-empty style="height: 100%;" v-else description="没有内容"></el-empty>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getNowLog
} from '@/api/logInfo'
import {
  Loading
} from 'element-ui';
export default {
  name: 'Dashboard',
  data() {
    return {
      activeTab:"info",
      queryNum: 100,
      logInfo: {
        name: "",
        size: "",
        content: ""
      },
      logLevels: ["ALL","INFO", "DEBUG", "ERROR", "WARN"], // Add more log levels as needed
      selectedLogLevel: "ALL",
      logArray:[]
    }
  },
  methods: {
    queryLog() {
      let loading = Loading.service({lock:true,text:"加载中"});
      getNowLog(this.queryNum).then(res=>{
        this.logInfo = res.data;
        this.splitLog();
      }).finally((err)=>{
        loading.close()
      })
    },
    splitLog() {
      this.logArray = this.logInfo.content.split('\n')
      for (let index = 0; index < this.logArray.length; index++) {
        if(this.logArray[index].includes("ERROR")) {
          this.logArray[index] = "<span style=\"color:red;\">" + this.logArray[index] + "</span>"
        }
      }
      this.logInfo.content = this.logArray.join("\n");
    },
    filterLogByLevel() {
      
    },
    logLevelChange() {
      let level = this.selectedLogLevel;
      let logs = this.logArray;
      let filterLogs = [];
      let flag = false;
      if(level == this.logLevels[0]) {
        this.logInfo.content = this.logArray.join("\n");
        return;
      }
      for (let index = 0; index < logs.length; index++) {
        let element = logs[index];
        if(element.includes(level)) {
          filterLogs.push(element);
          flag = true;
        }
        else if(element.includes(this.logLevels[1]) ||element.includes(this.logLevels[2])  ||
        element.includes(this.logLevels[3] || element.includes(this.logLevels[4]))) {
          flag = false;
        }else if(flag) {
          filterLogs.push(element);
        }
      }
      this.logInfo.content = filterLogs.join("\n");
    }
  },
  mounted() {
    getNowLog(100).then(response => {
      this.logInfo = response.data
      this.splitLog();
    })
  },

  beforeDestroy() {

  },
  watch: {
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }

}

.logTab {
  .pane {
    height: calc(100vh - 237px);
    overflow: auto;
  }

  pre {
    height: 100%;
  }
}

.dashboard-container {
  height: 80vh;
}

.info-header {
  background-color: #409eff;
  color: #fff;
  padding: 10px;
  font-size: 18px;
  border-bottom: 1px solid #ebeef5;
}

.flex-center {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>
