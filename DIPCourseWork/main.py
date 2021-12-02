import sys
import cv2
import time
from PyQt5.QtWidgets import QWidget, QDesktopWidget, QApplication, QLabel, QVBoxLayout, QGridLayout, QLineEdit
from PyQt5.QtWidgets import QHBoxLayout, QPushButton, QFileDialog, QMessageBox
from PyQt5.QtGui import QPixmap, QImage
from PyQt5.QtCore import QSize, Qt


# ##将cv2的图片能在QLabel中显示############################################################################
def convert_image(image):
    image = cv2.resize(image, (305, 285))

    if len(image.shape) == 2:
        ret, image = cv2.threshold(image, 0, 255, cv2.THRESH_OTSU | cv2.THRESH_BINARY)
        image = cv2.cvtColor(image, cv2.COLOR_GRAY2BGR)

    cv2.cvtColor(image, cv2.COLOR_BGR2RGB, image)
    qt_image = QImage(image.data, image.shape[1], image.shape[0], image.shape[1]*3, QImage.Format_RGB888)
    return qt_image
# #######################################################################################################


class Example(QWidget):
    def __init__(self):
        super().__init__()
        self.label1 = QLabel()
        self.label2 = QLabel()
        self.label3 = QLabel()
        self.label4 = QLabel()

        self.text1 = QLabel('前景视频')
        self.text2 = QLabel('掩膜')
        self.text3 = QLabel('背景视频')
        self.text4 = QLabel('合成视频')
        self.text5 = QLabel('视频合成期间：\n按ESC退出\n按空格暂停合成')

        self.picsize = QSize(305, 285)

        self.frontEdit = QLineEdit()
        self.backgroundEdit = QLineEdit()

        self.startButton = QPushButton('开始合成')
        self.clearButton = QPushButton('清空')

        self.flag = False   # 标志界面状态，是否在合成视频
        self.merge_state = 1    # 标志视频合成状态，1继续，2暂停，3结束

        self.initUI()  # 界面绘制交给InitUi方法

# ######控制窗口显示在屏幕中心###############################################################################
    def center(self):
        # 获得窗口
        qr = self.frameGeometry()
        # 获得屏幕中心点
        cp = QDesktopWidget().availableGeometry().center()
        # 显示到屏幕中心
        qr.moveCenter(cp)
        self.move(qr.topLeft())

    def keyPressEvent(self, event):
        if self.flag:
            if event.key() == Qt.Key_Escape:
                self.merge_state = 3
            if event.key() == Qt.Key_Space:
                self.merge_state = 2

# #######文件夹按钮处理####################################################################################
    def fileFront_click(self):
        s = self.inputFilePath()
        self.frontEdit.setText(s[0])

    def fileBackground_click(self):
        s = self.inputFilePath()
        self.backgroundEdit.setText(s[0])

# ######实现启动电脑资源管理器，做导入导出视频用########################################################
    def inputFilePath(self):
        file_path = QFileDialog.getOpenFileName(self, 'Open file', './', '*.avi; *.mp4')
        return file_path

    def outputFilePath(self):
        file_path = QFileDialog.getSaveFileName(self, 'Save file', './', 'vedio(*.avi)')
        return file_path

# ######处理开始合成按钮事件#########################################################################
    def button_click_merge(self):
        self.startButton.setEnabled(False)
        self.clearButton.setEnabled(False)
        f = self.frontEdit.text()
        b = self.backgroundEdit.text()

        if f == '' or b == '':
            QMessageBox.about(self, u'提示', u'请导入前景和背景视频！')
            self.startButton.setEnabled(True)
            self.clearButton.setEnabled(True)
            return

        # 询问是否保存合成视频
        save_address = ''
        is_save = False
        is_set = False

        reply = QMessageBox.question(self, '保存', '是否保存合成视频？', QMessageBox.Yes | QMessageBox.No,
                                     QMessageBox.Yes)

        if reply == QMessageBox.Yes:
            save_address = self.outputFilePath()[0]

        if save_address != '':
            fourcc = cv2.VideoWriter_fourcc(*'XVID')  # 设置保存图片格式
            is_save = True

        cap1 = cv2.VideoCapture(f)
        if not cap1.isOpened():
            QMessageBox.about(self, u'错误', u'前景视频打开出错！')
            self.startButton.setEnabled(True)
            self.clearButton.setEnabled(True)
            return

        cap2 = cv2.VideoCapture(b)
        if not cap2.isOpened():
            QMessageBox.about(self, u'错误', u'背景视频打开出错！')
            self.startButton.setEnabled(True)
            self.clearButton.setEnabled(True)
            return

        self.flag = True
        self.merge_state = 1
        fgbg = cv2.createBackgroundSubtractorMOG2()  # 混合高斯背景建模算法
        while cap1.isOpened() and cap2.isOpened():
            ret1, frame1 = cap1.read()  # 读取图片
            ret2, frame2 = cap2.read()

            if ret1 and ret2:
                rows, cols = frame2.shape[:2]
                frame1 = cv2.resize(frame1, (cols, rows))
                if is_save and not is_set:      # 设置保存视频时画面的大小
                    out = cv2.VideoWriter(save_address, fourcc, 10.0, (cols, rows), True)
                    is_set = True

                fgmask = fgbg.apply(frame1)
                contours, hierarchy = cv2.findContours(fgmask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

                ret, fgmask = cv2.threshold(fgmask, 200, 255, cv2.THRESH_BINARY)
                element = cv2.getStructuringElement(cv2.MORPH_CROSS, (3, 3))  # 形态学去噪
                fgmask = cv2.morphologyEx(fgmask, cv2.MORPH_OPEN, element)  # 开运算去噪
                fgmask_inv = cv2.bitwise_not(fgmask)

                frame1_bg = cv2.bitwise_and(frame1, frame1, mask=fgmask)
                frame2_fg = cv2.bitwise_and(frame2, frame2, mask=fgmask_inv)
                dst = cv2.add(frame1_bg, frame2_fg)

                for cont in contours:
                    area = cv2.contourArea(cont)  # 计算轮廓面积
                    if area < 300:  # 过滤面积小于10的形状
                        continue
                    rect = cv2.boundingRect(cont)  # 提取矩形坐标
                    cv2.rectangle(frame1, (rect[0], rect[1]), (rect[0] + rect[2], rect[1] + rect[3]), (0, 205, 205), 1)
                    # 原图上绘制矩形
                    cv2.rectangle(fgmask, (rect[0], rect[1]), (rect[0] + rect[2], rect[1] + rect[3]),
                                  (0xff, 0xff, 0xff), 1)
                    # 黑白前景上绘制矩形

                frame1_qt = convert_image(frame1)
                frame2_qt = convert_image(frame2)
                mask_qt = convert_image(fgmask)
                dst_qt = convert_image(dst)

                self.label1.setPixmap(QPixmap.fromImage(frame1_qt))
                self.label2.setPixmap(QPixmap.fromImage(mask_qt))
                self.label3.setPixmap(QPixmap.fromImage(frame2_qt))
                self.label4.setPixmap(QPixmap.fromImage(dst_qt))

                QApplication.processEvents()        # 刷新界面

                if is_save:
                    out.write(dst)

                if self.merge_state == 3:     # 按esc退出
                    cap1.release()
                    cap2.release()
                    if is_save:
                        out.release()
                    self.flag = False
                    self.startButton.setEnabled(True)
                    self.clearButton.setEnabled(True)
                    QMessageBox.about(self, u'退出', u'已停止视频合成！')
                    return

                if self.merge_state == 2:     # 按空格键暂停
                    resp = QMessageBox.question(self, '暂停', '视频合成已暂停！\n是否继续？',
                                                QMessageBox.Yes | QMessageBox.No, QMessageBox.Yes)
                    if resp == QMessageBox.No:
                        cap1.release()
                        cap2.release()
                        if is_save:
                            out.release()
                        self.flag = False
                        self.startButton.setEnabled(True)
                        self.clearButton.setEnabled(True)
                        return
                    else:
                        self.merge_state = 1    # 重置状态，继续播放

                time.sleep(0.03)

            else:
                break

        cap1.release()
        cap2.release()
        if is_save:
            out.release()
        self.flag = False
        self.startButton.setEnabled(True)
        self.clearButton.setEnabled(True)
        return

    def button_click_clear(self):       # 清空各组件内容
        self.frontEdit.clear()
        self.backgroundEdit.clear()

        pic = QPixmap("images\init.png").scaled(self.picsize)
        self.label1.setPixmap(pic)
        self.label2.setPixmap(pic)
        self.label3.setPixmap(pic)
        self.label4.setPixmap(pic)

        QApplication.processEvents()
##########################################################################################################

    def initUI(self):
        self.setFixedSize(1000, 600)

# ###########左侧窗口栏设计##################################################################
        pic = QPixmap("images\init.png").scaled(self.picsize)

        # 四个视频播放区，2*2网格布局
        grid = QGridLayout()

        self.label1.setPixmap(pic)
        self.label2.setPixmap(pic)
        self.label3.setPixmap(pic)
        self.label4.setPixmap(pic)

        grid.addWidget(self.label1, 0, 0)
        grid.addWidget(self.label2, 0, 1)
        grid.addWidget(self.text1, 1, 0, alignment=Qt.AlignHCenter)
        grid.addWidget(self.text2, 1, 1, alignment=Qt.AlignHCenter)
        grid.addWidget(self.label3, 2, 0)
        grid.addWidget(self.label4, 2, 1)
        grid.addWidget(self.text3, 3, 0, alignment=Qt.AlignHCenter)
        grid.addWidget(self.text4, 3, 1, alignment=Qt.AlignHCenter)
# ###########左侧窗口栏设计##################################################################

# ###########右侧菜单栏设计##################################################################
        # 垂直布局
        vbox_menu = QVBoxLayout()

        hbox_front = QHBoxLayout()
        front = QLabel('前景视频')
        fileFront = QPushButton()
        fileFront.setStyleSheet("QPushButton{border-image: url(images/folder.png)}")
        fileFront.setFixedSize(20, 20)
        fileFront.clicked.connect(self.fileFront_click)
        hbox_front.addWidget(front)
        hbox_front.addStretch(1)
        hbox_front.addWidget(fileFront)

        hbox_background = QHBoxLayout()
        background = QLabel('背景视频')
        fileBackground = QPushButton()
        fileBackground.setStyleSheet("QPushButton{border-image: url(images/folder.png)}")
        fileBackground.setFixedSize(20, 20)
        fileBackground.clicked.connect(self.fileBackground_click)
        hbox_background.addWidget(background)
        hbox_background.addStretch(1)
        hbox_background.addWidget(fileBackground)

        hbox_button = QHBoxLayout()
        vbox_button = QVBoxLayout()
        vbox_button.addWidget(self.startButton)
        vbox_button.addStretch(1)
        vbox_button.addWidget(self.clearButton)
        hbox_button.addStretch(1)
        hbox_button.addLayout(vbox_button)
        hbox_button.addStretch(1)
        # 绑定鼠标点击事件
        self.startButton.clicked.connect(self.button_click_merge)
        self.clearButton.clicked.connect(self.button_click_clear)

        # 右下角提示框
        hmsgbox = QHBoxLayout()
        hmsgbox.addStretch(1)
        hmsgbox.addWidget(self.text5)
        hmsgbox.addStretch(0.2)

        vbox_menu.addStretch(1)
        vbox_menu.addLayout(hbox_front)
        vbox_menu.addWidget(self.frontEdit)
        vbox_menu.addLayout(hbox_background)
        vbox_menu.addWidget(self.backgroundEdit)
        vbox_menu.addStretch(1)
        vbox_menu.addLayout(hbox_button)
        vbox_menu.addStretch(1)
        vbox_menu.addLayout(hmsgbox)

        hbox = QHBoxLayout()
        hbox.addLayout(grid)
        hbox.addLayout(vbox_menu)
# ###########右侧菜单栏设计##################################################################
        self.setLayout(hbox)
        self.center()
        self.setFocusPolicy(Qt.StrongFocus)
        self.show()


if __name__ == '__main__':
    # 创建应用程序和对象
    app = QApplication(sys.argv)
    ex = Example()
    sys.exit(app.exec_())
