<template>
  <el-card class="box-card">

    <div slot="header" class="clearfix">
      <span>
        <Mallki class-name="title" text="今日上课状态"></Mallki>
      </span>
      <el-button style="padding: 3px 0;float: right;" type="text">
        <Mallki class-name="date" :text="todayDate"></Mallki>
      </el-button>
    </div>
    <div class="text item">
      <el-row :gutter="12">
        <el-col :span="10">
          <x-chart v-show="isWeekend === false" class="yibiaoCharts" id="yibiaoCharts" :option="yibiaoCharts"></x-chart>
          <div v-show="isWeekend === true">
            今天木有课哦~
          </div>
        </el-col>

        <el-col :span="14">
          <el-tabs v-model="teacherWord.whichIsActive">
            <el-tab-pane label="课程评价" name="课程评价" active>
              <el-rate v-model="classStar.value" disabled text-color="#ff9900" score-template="{value}"
                       show-text></el-rate>
              上课要好好表现呀！
            </el-tab-pane>

            <el-tab-pane label="语音留言" name="语音留言">
              <Mallki class-name="teacher_name" :text="'来自'+todayCourse.teacherName+'老师的语音留言'"></Mallki>
              <VueAudio :theUrl="teacherWord.url"></VueAudio>
              <el-button icon="el-icon-sort" type="primary" @click="commentBack">
                回复
              </el-button>
            </el-tab-pane>

            <el-tab-pane label="文本留言" name="文本留言">
              <el-alert
                title="啊啊啊啊啊啊啊哈"
                type="success"
                :description="message"
                :closable="false">
              </el-alert>
            </el-tab-pane>

          </el-tabs>

        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
  import Mallki from "@/components/MyComponents/Mallki";
  import XChart from '@/components/charts/test'
  import VueAudio from "@/components/VueAudio";

  export default {
    name: "StateOfClass",
    components: {VueAudio, Mallki, XChart},
    data() {
      return {
        userData: {},
        teacherWord: {
          url: 'http://106.12.202.93/aaa.mp3',
          whichIsActive: '语音留言',
        },
        message: '还没有收到消息哟~',

        classStar: {
          value: 3,
          iconClasses: ['icon-rate-face-1', 'icon-rate-face-2', 'icon-rate-face-3'],
        },

        isWeekend: false, // 周末？或者今天没有课
        todayCourse: {},
        todayDate: '',

        yibiaoCharts: {
          chart: {
            type: 'solidgauge',
            marginTop: 50,
            renderTo: 'yibiaoCharts',
          },
          colors: ['#F62366', '#9DFF02', '#0CCDD6'],
          title: {
            text: ' ',
          },
          credits: {
            enabled: false, // 右下角的 highcharts 标识去掉
          },
          tooltip: {
            borderWidth: 0,
            backgroundColor: 'none',
            shadow: false,
            style: {
              fontSize: '16px',
              color: 'silver'
            },
            pointFormat: '{series.name}<br><span style="font-size:2em; color: {point.color}; font-weight: bold">{point.y}分</span>',
            positioner: function (labelWidth) {
              return {
                x: 200 - labelWidth / 2,
                y: 180
              };
            }
          },
          pane: {
            startAngle: 0,
            endAngle: 360,
            background: [
              // { // Track for Move
              //   outerRadius: '112%',
              //   innerRadius: '88%',
              //   //Highcharts.Color(Highcharts.getOptions().colors[1]).setOpacity(0.3).get(),
              //   borderWidth: 0
              // },
              // { // Track for Exercise
              //   outerRadius: '87%',
              //   innerRadius: '63%',
              //   // backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[1]).setOpacity(0.3).get(),
              //   borderWidth: 0
              // },
              // { // Track for Stand
              //   outerRadius: '62%',
              //   innerRadius: '38%',
              //   // backgroundColor: Highcharts.Color(Highcharts.getOptions().colors[2]).setOpacity(0.3).get(),
              //   borderWidth: 0
              // }
            ]
          },
          yAxis: {
            min: 0,
            max: 100,
            lineWidth: 0,
            tickPositions: []
          },
          plotOptions: {
            solidgauge: {
              borderWidth: '34px',
              dataLabels: {
                enabled: false
              },
              linecap: 'round',
              stickyTracking: false
            }
          },

          series: [
            {
              name: '考勤总分',
              borderColor: '#0CCDD6',
              data: [
                {
                  color: '#0CCDD6',
                  radius: '75%',
                  innerRadius: '75%',
                  y: 0,
                }
              ]
            },
            {
              name: '专注度',
              borderColor: '#9DFF02',
              data: [
                {
                  color: '#9DFF02',
                  radius: '50%',
                  innerRadius: '50%',
                  y: 0,
                }
              ]
            },
          ]
        }, // 活动仪表图
      }
    },
    async mounted() {
      this.userData = JSON.parse(sessionStorage.getItem('userData'));

      // 获取学生消息
      // this.$http.post('wisdom_web/monitorStudent/getStudentMsg', {
      //   id: this.userData.id
      // }).then(res => {
      //   localStorage.setItem('message', JSON.stringify(res.body));
      //   if (res.body.data !== null) {
      //     this.message = res.body.data;
      //   }
      // }).catch(err => {
      //   console.log(err);
      // });

      // 如果是周六、周日就不显示这一栏
      if (new Date().getDay() === 6 || new Date().getDay() === 0) {
        this.isWeekend = true;
        return;
      }

      // 获取今天的上课状态信息
      await this.$http.post('wisdom_web/studentCourseInfo/today', {}).then(res => {
        if (res.body.data.length === 0) {
          this.isWeekend = true;
        } else {
          this.todayCourse = res.body.data[0];
        }
      }).catch(err => {
        console.log("--------err in Student/ShowInfo/StateOfClass-------");
        console.log(err);
      });

      this.todayDate = new Date().toLocaleDateString();
      this.yibiaoCharts.series[0].data[0].y = this.todayCourse.attendanceTotalScore;
      this.yibiaoCharts.series[1].data[0].y = this.todayCourse.headUpScore;

    },
    methods: {
      commentBack() {
        this.$prompt('', '回复', {
          confirmButtonText: '提交',
          cancelButtonText: '取消',
        }).then(({ value }) => {
          this.$message({
            type: 'success',
            message: '已发送',
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消'
          });
        });
      },
    }
  }
</script>

<style scoped>

</style>
