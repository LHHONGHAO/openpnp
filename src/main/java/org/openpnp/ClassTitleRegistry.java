package org.openpnp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class ClassTitleRegistry {
    
    public static class ClassMapping {
        public final String id;
        public final String className;
        public final String simpleName;
        public final String translationKey;
        public final String englishTitle;
        public final String chineseTitle;
        public final boolean appendName;
        public final String category;
        
        public ClassMapping(String id, String className, String simpleName, 
                String translationKey, String englishTitle, String chineseTitle, 
                boolean appendName, String category) {
            this.id = id;
            this.className = className;
            this.simpleName = simpleName;
            this.translationKey = translationKey;
            this.englishTitle = englishTitle;
            this.chineseTitle = chineseTitle;
            this.appendName = appendName;
            this.category = category;
        }
    }
    
    private static final List<ClassMapping> mappings = new ArrayList<>();
    private static final Map<String, ClassMapping> mappingsByClassName = new HashMap<>();
    private static final Map<String, ClassMapping> mappingsBySimpleName = new HashMap<>();
    private static final Map<String, ClassMapping> mappingsById = new HashMap<>();
    private static final Preferences prefs = Preferences.userNodeForPackage(ClassTitleRegistry.class);
    
    // 文本映射 - 用于问题/解决方案等文本内容的映射
    public static class TextMapping {
        public final String id;
        public final String text;
        public final String englishTranslation;
        public final String chineseTranslation;
        
        public TextMapping(String id, String text, String englishTranslation, String chineseTranslation) {
            this.id = id;
            this.text = text;
            this.englishTranslation = englishTranslation;
            this.chineseTranslation = chineseTranslation;
        }
    }
    
    // 模式匹配文本映射 - 用于包含动态名称的文本（如"Set the manual nozzle tip change location for N1."）
    public static class TextMappingPattern {
        public final String id;
        public final java.util.regex.Pattern pattern;
        public final String englishTemplate;
        public final String chineseTemplate;
        
        public TextMappingPattern(String id, String regex, String englishTemplate, String chineseTemplate) {
            this.id = id;
            this.pattern = java.util.regex.Pattern.compile(regex);
            this.englishTemplate = englishTemplate;
            this.chineseTemplate = chineseTemplate;
        }
    }
    
    private static final List<TextMapping> textMappings = new ArrayList<>();
    private static final Map<String, TextMapping> textMappingsByText = new HashMap<>();
    private static final Map<String, TextMapping> textMappingsById = new HashMap<>();
    
    private static final List<TextMappingPattern> textMappingPatterns = new ArrayList<>();
    
    // 映射启用开关
    private static final String PREF_MAPPING_ENABLED = "mappingEnabled";
    
    public static boolean isMappingEnabled() {
        return prefs.getBoolean(PREF_MAPPING_ENABLED, false);
    }
    
    public static void setMappingEnabled(boolean enabled) {
        prefs.putBoolean(PREF_MAPPING_ENABLED, enabled);
        try {
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        // Machine components
        addMapping(new ClassMapping("ID_001", "org.openpnp.machine.reference.ReferenceMachine", 
                "ReferenceMachine", "ClassTitle.ID_001", "Machine", "机器", false, "Machine"));
        addMapping(new ClassMapping("ID_002", "org.openpnp.machine.reference.ReferenceHead", 
                "ReferenceHead", "ClassTitle.ID_002", "Head", "贴装头", true, "Machine"));
        addMapping(new ClassMapping("ID_003", "org.openpnp.machine.reference.ReferenceNozzle", 
                "ReferenceNozzle", "ClassTitle.ID_003", "Nozzle", "吸嘴", true, "Machine"));
        addMapping(new ClassMapping("ID_004", "org.openpnp.machine.reference.ReferenceNozzleTip", 
                "ReferenceNozzleTip", "ClassTitle.ID_004", "Nozzle Tip", "吸嘴尖", true, "Machine"));
        addMapping(new ClassMapping("ID_005", "org.openpnp.machine.reference.ReferenceActuator", 
                "ReferenceActuator", "ClassTitle.ID_005", "Actuator", "执行器", true, "Machine"));
        addMapping(new ClassMapping("ID_006", "org.openpnp.machine.reference.HttpActuator", 
                "HttpActuator", "ClassTitle.ID_006", "HTTP Actuator", "HTTP执行器", true, "Machine"));
        addMapping(new ClassMapping("ID_007", "org.openpnp.machine.reference.ScriptActuator", 
                "ScriptActuator", "ClassTitle.ID_007", "Script Actuator", "脚本执行器", true, "Machine"));
        addMapping(new ClassMapping("ID_008", "org.openpnp.machine.reference.ContactProbeNozzle", 
                "ContactProbeNozzle", "ClassTitle.ID_008", "Contact Probe Nozzle", "接触探针吸嘴", true, "Machine"));
        addMapping(new ClassMapping("ID_009", "org.openpnp.machine.reference.ReferenceNozzleTipCalibration", 
                "ReferenceNozzleTipCalibration", "ClassTitle.ID_009", "Nozzle Tip Calibration", "吸嘴尖校准", false, "Machine"));
        addMapping(new ClassMapping("ID_010", "org.openpnp.machine.reference.ReferenceActuatorProfiles", 
                "ReferenceActuatorProfiles", "ClassTitle.ID_010", "Actuator Profiles", "执行器配置", false, "Machine"));
        addMapping(new ClassMapping("ID_011", "org.openpnp.machine.reference.ActuatorInterlockMonitor", 
                "ActuatorInterlockMonitor", "ClassTitle.ID_011", "Actuator Interlock Monitor", "执行器互锁监控", false, "Machine"));
        addMapping(new ClassMapping("ID_012", "org.openpnp.machine.reference.SimulationModeMachine", 
                "SimulationModeMachine", "ClassTitle.ID_012", "Simulation Mode", "模拟模式", false, "Machine"));
        addMapping(new ClassMapping("ID_013", "org.openpnp.machine.reference.ReferenceCameraBatchOperation", 
                "ReferenceCameraBatchOperation", "ClassTitle.ID_013", "Camera Batch Operation", "相机批量操作", false, "Machine"));
        addMapping(new ClassMapping("ID_014", "org.openpnp.machine.reference.ReferencePnpJobProcessor", 
                "ReferencePnpJobProcessor", "ClassTitle.ID_014", "PnP Job Processor", "贴装作业处理器", false, "Machine"));
        
        // Axes
        addMapping(new ClassMapping("ID_015", "org.openpnp.machine.reference.axis.ReferenceControllerAxis", 
                "ReferenceControllerAxis", "ClassTitle.ID_015", "Controller Axis", "控制器轴", true, "Axis"));
        addMapping(new ClassMapping("ID_016", "org.openpnp.machine.reference.axis.ReferenceVirtualAxis", 
                "ReferenceVirtualAxis", "ClassTitle.ID_016", "Virtual Axis", "虚拟轴", true, "Axis"));
        addMapping(new ClassMapping("ID_017", "org.openpnp.machine.reference.axis.ReferenceCamClockwiseAxis", 
                "ReferenceCamClockwiseAxis", "ClassTitle.ID_017", "Cam Clockwise Axis", "凸轮顺时针轴", true, "Axis"));
        addMapping(new ClassMapping("ID_018", "org.openpnp.machine.reference.axis.ReferenceCamCounterClockwiseAxis", 
                "ReferenceCamCounterClockwiseAxis", "ClassTitle.ID_018", "Cam Counter-Clockwise Axis", "凸轮逆时针轴", true, "Axis"));
        addMapping(new ClassMapping("ID_019", "org.openpnp.machine.reference.axis.ReferenceLinearTransformAxis", 
                "ReferenceLinearTransformAxis", "ClassTitle.ID_019", "Linear Transform Axis", "线性变换轴", true, "Axis"));
        addMapping(new ClassMapping("ID_020", "org.openpnp.machine.reference.axis.ReferenceMappedAxis", 
                "ReferenceMappedAxis", "ClassTitle.ID_020", "Mapped Axis", "映射轴", true, "Axis"));
        addMapping(new ClassMapping("ID_021", "org.openpnp.spi.base.AbstractAxis", 
                "AbstractAxis", "ClassTitle.ID_021", "Axis", "轴", true, "Axis"));
        
        // Feeders
        addMapping(new ClassMapping("ID_022", "org.openpnp.machine.reference.feeder.ReferenceStripFeeder", 
                "ReferenceStripFeeder", "ClassTitle.ID_022", "Strip Feeder", "条状送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_023", "org.openpnp.machine.reference.feeder.ReferenceTrayFeeder", 
                "ReferenceTrayFeeder", "ClassTitle.ID_023", "Tray Feeder", "托盘送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_024", "org.openpnp.machine.reference.feeder.ReferenceTubeFeeder", 
                "ReferenceTubeFeeder", "ClassTitle.ID_024", "Tube Feeder", "管状送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_025", "org.openpnp.machine.reference.feeder.ReferenceLoosePartFeeder", 
                "ReferenceLoosePartFeeder", "ClassTitle.ID_025", "Loose Part Feeder", "松散元件送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_026", "org.openpnp.machine.reference.feeder.ReferencePushPullFeeder", 
                "ReferencePushPullFeeder", "ClassTitle.ID_026", "Push-Pull Feeder", "推拉力送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_027", "org.openpnp.machine.reference.feeder.ReferenceRotatedTrayFeeder", 
                "ReferenceRotatedTrayFeeder", "ClassTitle.ID_027", "Rotated Tray Feeder", "旋转托盘送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_028", "org.openpnp.machine.reference.feeder.ReferenceDragFeeder", 
                "ReferenceDragFeeder", "ClassTitle.ID_028", "Drag Feeder", "拖拽送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_029", "org.openpnp.machine.reference.feeder.ReferenceHeapFeeder", 
                "ReferenceHeapFeeder", "ClassTitle.ID_029", "Heap Feeder", "堆积送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_030", "org.openpnp.machine.reference.feeder.ReferenceLeverFeeder", 
                "ReferenceLeverFeeder", "ClassTitle.ID_030", "Lever Feeder", "杠杆送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_031", "org.openpnp.machine.reference.feeder.ReferenceAutoFeeder", 
                "ReferenceAutoFeeder", "ClassTitle.ID_031", "Auto Feeder", "自动送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_032", "org.openpnp.machine.reference.feeder.ReferenceSlotAutoFeeder", 
                "ReferenceSlotAutoFeeder", "ClassTitle.ID_032", "Slot Auto Feeder", "槽式自动送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_033", "org.openpnp.machine.reference.feeder.AdvancedLoosePartFeeder", 
                "AdvancedLoosePartFeeder", "ClassTitle.ID_033", "Advanced Loose Part Feeder", "高级松散元件送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_034", "org.openpnp.machine.reference.feeder.BlindsFeeder", 
                "BlindsFeeder", "ClassTitle.ID_034", "Blinds Feeder", "百叶窗送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_035", "org.openpnp.machine.reference.feeder.SchultzFeeder", 
                "SchultzFeeder", "ClassTitle.ID_035", "Schultz Feeder", "舒尔茨送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_036", "org.openpnp.machine.reference.feeder.SlotSchultzFeeder", 
                "SlotSchultzFeeder", "ClassTitle.ID_036", "Slot Schultz Feeder", "槽式舒尔茨送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_037", "org.openpnp.machine.rapidplacer.RapidFeeder", 
                "RapidFeeder", "ClassTitle.ID_037", "Rapid Feeder", "快速送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_038", "org.openpnp.machine.photon.PhotonFeeder", 
                "PhotonFeeder", "ClassTitle.ID_038", "Photon Feeder", "光子送料器", true, "Feeder"));
        addMapping(new ClassMapping("ID_039", "org.openpnp.machine.pandaplacer.BambooFeederAutoVision", 
                "BambooFeederAutoVision", "ClassTitle.ID_039", "Bamboo Feeder Auto Vision", "竹制送料器自动视觉", true, "Feeder"));
        addMapping(new ClassMapping("ID_040", "org.openpnp.machine.neoden4.Neoden4Feeder", 
                "Neoden4Feeder", "ClassTitle.ID_040", "Neoden4 Feeder", "Neoden4送料器", true, "Feeder"));
        
        // Cameras
        addMapping(new ClassMapping("ID_041", "org.openpnp.machine.reference.camera.OpenPnpCaptureCamera", 
                "OpenPnpCaptureCamera", "ClassTitle.ID_041", "OpenPnP Capture Camera", "OpenPnP采集相机", true, "Camera"));
        addMapping(new ClassMapping("ID_042", "org.openpnp.machine.reference.camera.MjpgCaptureCamera", 
                "MjpgCaptureCamera", "ClassTitle.ID_042", "MJPG Capture Camera", "MJPG采集相机", true, "Camera"));
        addMapping(new ClassMapping("ID_043", "org.openpnp.machine.reference.camera.OpenCvCamera", 
                "OpenCvCamera", "ClassTitle.ID_043", "OpenCV Camera", "OpenCV相机", true, "Camera"));
        addMapping(new ClassMapping("ID_044", "org.openpnp.machine.reference.camera.ImageCamera", 
                "ImageCamera", "ClassTitle.ID_044", "Image Camera", "图像相机", true, "Camera"));
        addMapping(new ClassMapping("ID_045", "org.openpnp.machine.reference.camera.BufferedImageCamera", 
                "BufferedImageCamera", "ClassTitle.ID_045", "Buffered Image Camera", "缓冲图像相机", true, "Camera"));
        addMapping(new ClassMapping("ID_046", "org.openpnp.machine.reference.camera.GstreamerCamera", 
                "GstreamerCamera", "ClassTitle.ID_046", "GStreamer Camera", "GStreamer相机", true, "Camera"));
        addMapping(new ClassMapping("ID_047", "org.openpnp.machine.reference.camera.Webcams", 
                "Webcams", "ClassTitle.ID_047", "Webcams", "网络摄像头", true, "Camera"));
        addMapping(new ClassMapping("ID_048", "org.openpnp.machine.reference.camera.SimulatedUpCamera", 
                "SimulatedUpCamera", "ClassTitle.ID_048", "Simulated Up Camera", "模拟顶部相机", true, "Camera"));
        addMapping(new ClassMapping("ID_049", "org.openpnp.machine.reference.camera.SwitcherCamera", 
                "SwitcherCamera", "ClassTitle.ID_049", "Switcher Camera", "切换相机", true, "Camera"));
        addMapping(new ClassMapping("ID_050", "org.openpnp.machine.reference.camera.OnvifIPCamera", 
                "OnvifIPCamera", "ClassTitle.ID_050", "ONVIF IP Camera", "ONVIF IP相机", true, "Camera"));
        addMapping(new ClassMapping("ID_051", "org.openpnp.machine.reference.camera.AbstractBroadcastingCamera", 
                "AbstractBroadcastingCamera", "ClassTitle.ID_051", "Broadcasting Camera", "广播相机", true, "Camera"));
        addMapping(new ClassMapping("ID_052", "org.openpnp.machine.reference.camera.AbstractSettlingCamera", 
                "AbstractSettlingCamera", "ClassTitle.ID_052", "Settling Camera", "稳定相机", true, "Camera"));
        addMapping(new ClassMapping("ID_053", "org.openpnp.machine.reference.camera.ReferenceCamera", 
                "ReferenceCamera", "ClassTitle.ID_053", "Reference Camera", "参考相机", true, "Camera"));
        addMapping(new ClassMapping("ID_054", "org.openpnp.machine.reference.camera.AutoFocusProvider", 
                "AutoFocusProvider", "ClassTitle.ID_054", "Auto Focus Provider", "自动对焦提供者", false, "Camera"));
        addMapping(new ClassMapping("ID_055", "org.openpnp.machine.neoden4.Neoden4Camera", 
                "Neoden4Camera", "ClassTitle.ID_055", "Neoden4 Camera", "Neoden4相机", true, "Camera"));
        addMapping(new ClassMapping("ID_056", "org.openpnp.machine.neoden4.Neoden4SwitcherCamera", 
                "Neoden4SwitcherCamera", "ClassTitle.ID_056", "Neoden4 Switcher Camera", "Neoden4切换相机", true, "Camera"));
        addMapping(new ClassMapping("ID_057", "org.openpnp.machine.neoden4.Neoden4FeederActuator", 
                "Neoden4FeederActuator", "ClassTitle.ID_057", "Neoden4 Feeder Actuator", "Neoden4送料器执行器", true, "Machine"));
        
        // Drivers
        addMapping(new ClassMapping("ID_058", "org.openpnp.spi.base.AbstractDriver", 
                "AbstractDriver", "ClassTitle.ID_058", "Driver", "驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_059", "org.openpnp.machine.reference.driver.AbstractReferenceDriver", 
                "AbstractReferenceDriver", "ClassTitle.ID_059", "Reference Driver", "参考驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_060", "org.openpnp.machine.reference.driver.GcodeDriver", 
                "GcodeDriver", "ClassTitle.ID_060", "Gcode Driver", "Gcode驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_061", "org.openpnp.machine.reference.driver.GcodeAsyncDriver", 
                "GcodeAsyncDriver", "ClassTitle.ID_061", "Gcode Async Driver", "Gcode异步驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_062", "org.openpnp.machine.reference.driver.NullDriver", 
                "NullDriver", "ClassTitle.ID_062", "Null Driver", "空驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_063", "org.openpnp.machine.neoden4.Neoden4Driver", 
                "Neoden4Driver", "ClassTitle.ID_063", "Neoden4 Driver", "Neoden4驱动", true, "Driver"));
        addMapping(new ClassMapping("ID_064", "org.openpnp.machine.neoden4.Neoden4CameraDriver", 
                "Neoden4CameraDriver", "ClassTitle.ID_064", "Neoden4 Camera Driver", "Neoden4相机驱动", true, "Driver"));
        
        // Motion Planners
        addMapping(new ClassMapping("ID_065", "org.openpnp.machine.reference.driver.AbstractMotionPlanner", 
                "AbstractMotionPlanner", "ClassTitle.ID_065", "Motion Planner", "运动规划器", true, "Driver"));
        addMapping(new ClassMapping("ID_066", "org.openpnp.machine.reference.driver.NullMotionPlanner", 
                "NullMotionPlanner", "ClassTitle.ID_066", "Null Motion Planner", "空运动规划器", true, "Driver"));
        addMapping(new ClassMapping("ID_067", "org.openpnp.machine.reference.driver.ReferenceAdvancedMotionPlanner", 
                "ReferenceAdvancedMotionPlanner", "ClassTitle.ID_067", "Advanced Motion Planner", "高级运动规划器", true, "Driver"));
        
        // Vision
        addMapping(new ClassMapping("ID_068", "org.openpnp.machine.reference.vision.ReferenceFiducialLocator", 
                "ReferenceFiducialLocator", "ClassTitle.ID_068", "Reference Fiducial Locator", "基准点定位器", false, "Vision"));
        addMapping(new ClassMapping("ID_069", "org.openpnp.machine.reference.vision.ReferenceBottomVision", 
                "ReferenceBottomVision", "ClassTitle.ID_069", "Reference Bottom Vision", "底部视觉", false, "Vision"));
        addMapping(new ClassMapping("ID_070", "org.openpnp.machine.reference.vision.OpenCvVisionProvider", 
                "OpenCvVisionProvider", "ClassTitle.ID_070", "OpenCV Vision Provider", "OpenCV视觉提供者", false, "Vision"));
        addMapping(new ClassMapping("ID_071", "org.openpnp.machine.reference.vision.AbstractPartAlignment", 
                "AbstractPartAlignment", "ClassTitle.ID_071", "Part Alignment", "元件对齐", false, "Vision"));
        addMapping(new ClassMapping("ID_072", "org.openpnp.machine.reference.vision.AbstractPartSettingsHolder", 
                "AbstractPartSettingsHolder", "ClassTitle.ID_072", "Part Settings Holder", "元件设置持有者", false, "Vision"));
        
        // Signalers
        addMapping(new ClassMapping("ID_073", "org.openpnp.spi.base.AbstractSignaler", 
                "AbstractSignaler", "ClassTitle.ID_073", "Signaler", "信号器", true, "Signaler"));
        addMapping(new ClassMapping("ID_074", "org.openpnp.machine.reference.signaler.ActuatorSignaler", 
                "ActuatorSignaler", "ClassTitle.ID_074", "Actuator Signaler", "执行器信号器", true, "Signaler"));
        addMapping(new ClassMapping("ID_075", "org.openpnp.machine.reference.signaler.SoundSignaler", 
                "SoundSignaler", "ClassTitle.ID_075", "Sound Signaler", "声音信号器", true, "Signaler"));
        addMapping(new ClassMapping("ID_076", "org.openpnp.machine.neoden4.Neoden4Signaler", 
                "Neoden4Signaler", "ClassTitle.ID_076", "Neoden4 Signaler", "Neoden4信号器", true, "Signaler"));
        
        // Other
        addMapping(new ClassMapping("ID_077", "org.openpnp.spi.base.SimplePropertySheetHolder", 
                "SimplePropertySheetHolder", "ClassTitle.ID_077", "Property Sheet Holder", "属性表持有者", false, "Other"));
        addMapping(new ClassMapping("ID_078", "org.openpnp.spi.base.AbstractJobProcessor", 
                "AbstractJobProcessor", "ClassTitle.ID_078", "Job Processor", "作业处理器", false, "Other"));
        
        // Pipeline stages
        addMapping(new ClassMapping("ID_079", "org.openpnp.vision.pipeline.stages.AffineWarp", 
                "AffineWarp", "ClassTitle.ID_079", "Affine Warp", "仿射变换", false, "Pipeline"));
        addMapping(new ClassMapping("ID_080", "org.openpnp.vision.pipeline.stages.BlurGaussian", 
                "BlurGaussian", "ClassTitle.ID_080", "Blur Gaussian", "高斯模糊", false, "Pipeline"));
        addMapping(new ClassMapping("ID_081", "org.openpnp.vision.pipeline.stages.BlurMedian", 
                "BlurMedian", "ClassTitle.ID_081", "Blur Median", "中值模糊", false, "Pipeline"));
        addMapping(new ClassMapping("ID_082", "org.openpnp.vision.pipeline.stages.ClosestModel", 
                "ClosestModel", "ClassTitle.ID_082", "Closest Model", "最近模型", false, "Pipeline"));
        addMapping(new ClassMapping("ID_083", "org.openpnp.vision.pipeline.stages.ClosestModelPoint", 
                "ClosestModelPoint", "ClassTitle.ID_083", "Closest Model Point", "最近模型点", false, "Pipeline"));
        addMapping(new ClassMapping("ID_084", "org.openpnp.vision.pipeline.stages.ColorDistance", 
                "ColorDistance", "ClassTitle.ID_084", "Color Distance", "颜色距离", false, "Pipeline"));
        addMapping(new ClassMapping("ID_085", "org.openpnp.vision.pipeline.stages.ConcatenateModels", 
                "ConcatenateModels", "ClassTitle.ID_085", "Concatenate Models", "连接模型", false, "Pipeline"));
        addMapping(new ClassMapping("ID_086", "org.openpnp.vision.pipeline.stages.ConvertModelToPoints", 
                "ConvertModelToPoints", "ClassTitle.ID_086", "Convert Model To Points", "转换模型到点", false, "Pipeline"));
        addMapping(new ClassMapping("ID_087", "org.openpnp.vision.pipeline.stages.ConvertToMask", 
                "ConvertToMask", "ClassTitle.ID_087", "Convert To Mask", "转换为遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_088", "org.openpnp.vision.pipeline.stages.CorrectDistortion", 
                "CorrectDistortion", "ClassTitle.ID_088", "Correct Distortion", "校正畸变", false, "Pipeline"));
        addMapping(new ClassMapping("ID_089", "org.openpnp.vision.pipeline.stages.Crop", 
                "Crop", "ClassTitle.ID_089", "Crop", "裁剪", false, "Pipeline"));
        addMapping(new ClassMapping("ID_090", "org.openpnp.vision.pipeline.stages.CircleDetector", 
                "CircleDetector", "ClassTitle.ID_090", "Circle Detector", "圆形检测", false, "Pipeline"));
        addMapping(new ClassMapping("ID_091", "org.openpnp.vision.pipeline.stages.DetectCirclesHough", 
                "DetectCirclesHough", "ClassTitle.ID_091", "Detect Circles Hough", "检测圆形（霍夫）", false, "Pipeline"));
        addMapping(new ClassMapping("ID_092", "org.openpnp.vision.pipeline.stages.DetectEdgesCanny", 
                "DetectEdgesCanny", "ClassTitle.ID_092", "Detect Edges Canny", "检测边缘（Canny）", false, "Pipeline"));
        addMapping(new ClassMapping("ID_093", "org.openpnp.vision.pipeline.stages.DetectEdgesLaplace", 
                "DetectEdgesLaplace", "ClassTitle.ID_093", "Detect Edges Laplace", "检测边缘（Laplace）", false, "Pipeline"));
        addMapping(new ClassMapping("ID_094", "org.openpnp.vision.pipeline.stages.DetectFixedCircles", 
                "DetectFixedCircles", "ClassTitle.ID_094", "Detect Fixed Circles", "检测固定圆形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_095", "org.openpnp.vision.pipeline.stages.DetectFixedRect", 
                "DetectFixedRect", "ClassTitle.ID_095", "Detect Fixed Rect", "检测固定矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_096", "org.openpnp.vision.pipeline.stages.DetectFixedRectFromCenter", 
                "DetectFixedRectFromCenter", "ClassTitle.ID_096", "Detect Fixed Rect From Center", "从中心检测固定矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_097", "org.openpnp.vision.pipeline.stages.DetectKnownCircles", 
                "DetectKnownCircles", "ClassTitle.ID_097", "Detect Known Circles", "检测已知圆形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_098", "org.openpnp.vision.pipeline.stages.DetectKnownEllipses", 
                "DetectKnownEllipses", "ClassTitle.ID_098", "Detect Known Ellipses", "检测已知椭圆", false, "Pipeline"));
        addMapping(new ClassMapping("ID_099", "org.openpnp.vision.pipeline.stages.DetectKnownRect", 
                "DetectKnownRect", "ClassTitle.ID_099", "Detect Known Rect", "检测已知矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_100", "org.openpnp.vision.pipeline.stages.DetectLinesHough", 
                "DetectLinesHough", "ClassTitle.ID_100", "Detect Lines Hough", "检测直线（霍夫）", false, "Pipeline"));
        addMapping(new ClassMapping("ID_101", "org.openpnp.vision.pipeline.stages.DetectRectangleHough", 
                "DetectRectangleHough", "ClassTitle.ID_101", "Detect Rectangle Hough", "检测矩形（霍夫）", false, "Pipeline"));
        addMapping(new ClassMapping("ID_102", "org.openpnp.vision.pipeline.stages.DetectRectlinearSymmetry", 
                "DetectRectlinearSymmetry", "ClassTitle.ID_102", "Detect Rectlinear Symmetry", "检测矩形对称", false, "Pipeline"));
        addMapping(new ClassMapping("ID_103", "org.openpnp.vision.pipeline.stages.DilateModel", 
                "DilateModel", "ClassTitle.ID_103", "Dilate Model", "膨胀模型", false, "Pipeline"));
        addMapping(new ClassMapping("ID_104", "org.openpnp.vision.pipeline.stages.DrawCircles", 
                "DrawCircles", "ClassTitle.ID_104", "Draw Circles", "绘制圆形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_105", "org.openpnp.vision.pipeline.stages.DrawContours", 
                "DrawContours", "ClassTitle.ID_105", "Draw Contours", "绘制轮廓", false, "Pipeline"));
        addMapping(new ClassMapping("ID_106", "org.openpnp.vision.pipeline.stages.DrawEllipses", 
                "DrawEllipses", "ClassTitle.ID_106", "Draw Ellipses", "绘制椭圆", false, "Pipeline"));
        addMapping(new ClassMapping("ID_107", "org.openpnp.vision.pipeline.stages.DrawImageCenter", 
                "DrawImageCenter", "ClassTitle.ID_107", "Draw Image Center", "绘制图像中心", false, "Pipeline"));
        addMapping(new ClassMapping("ID_108", "org.openpnp.vision.pipeline.stages.DrawKeyPoints", 
                "DrawKeyPoints", "ClassTitle.ID_108", "Draw KeyPoints", "绘制关键点", false, "Pipeline"));
        addMapping(new ClassMapping("ID_109", "org.openpnp.vision.pipeline.stages.DrawRotatedRects", 
                "DrawRotatedRects", "ClassTitle.ID_109", "Draw Rotated Rects", "绘制旋转矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_110", "org.openpnp.vision.pipeline.stages.DrawTemplateMatches", 
                "DrawTemplateMatches", "ClassTitle.ID_110", "Draw Template Matches", "绘制模板匹配", false, "Pipeline"));
        addMapping(new ClassMapping("ID_111", "org.openpnp.vision.pipeline.stages.FilterContours", 
                "FilterContours", "ClassTitle.ID_111", "Filter Contours", "过滤轮廓", false, "Pipeline"));
        addMapping(new ClassMapping("ID_112", "org.openpnp.vision.pipeline.stages.FilterRects", 
                "FilterRects", "ClassTitle.ID_112", "Filter Rects", "过滤矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_113", "org.openpnp.vision.pipeline.stages.FindContours", 
                "FindContours", "ClassTitle.ID_113", "Find Contours", "查找轮廓", false, "Pipeline"));
        addMapping(new ClassMapping("ID_114", "org.openpnp.vision.pipeline.stages.FitEllipseContours", 
                "FitEllipseContours", "ClassTitle.ID_114", "Fit Ellipse Contours", "拟合椭圆轮廓", false, "Pipeline"));
        addMapping(new ClassMapping("ID_115", "org.openpnp.vision.pipeline.stages.GrabCut", 
                "GrabCut", "ClassTitle.ID_115", "Grab Cut", "图割", false, "Pipeline"));
        addMapping(new ClassMapping("ID_116", "org.openpnp.vision.pipeline.stages.HistogramEqualize", 
                "HistogramEqualize", "ClassTitle.ID_116", "Histogram Equalize", "直方图均衡化", false, "Pipeline"));
        addMapping(new ClassMapping("ID_117", "org.openpnp.vision.pipeline.stages.HistogramEqualizeAdaptive", 
                "HistogramEqualizeAdaptive", "ClassTitle.ID_117", "Adaptive Histogram Equalize", "自适应直方图均衡化", false, "Pipeline"));
        addMapping(new ClassMapping("ID_118", "org.openpnp.vision.pipeline.stages.ImageCapture", 
                "ImageCapture", "ClassTitle.ID_118", "Image Capture", "图像采集", false, "Pipeline"));
        addMapping(new ClassMapping("ID_119", "org.openpnp.vision.pipeline.stages.ImageRead", 
                "ImageRead", "ClassTitle.ID_119", "Image Read", "读取图像", false, "Pipeline"));
        addMapping(new ClassMapping("ID_120", "org.openpnp.vision.pipeline.stages.ImageRecall", 
                "ImageRecall", "ClassTitle.ID_120", "Image Recall", "图像恢复", false, "Pipeline"));
        addMapping(new ClassMapping("ID_121", "org.openpnp.vision.pipeline.stages.ImageWrite", 
                "ImageWrite", "ClassTitle.ID_121", "Image Write", "写入图像", false, "Pipeline"));
        addMapping(new ClassMapping("ID_122", "org.openpnp.vision.pipeline.stages.ImageWriteDebug", 
                "ImageWriteDebug", "ClassTitle.ID_122", "Image Write Debug", "写入调试图像", false, "Pipeline"));
        addMapping(new ClassMapping("ID_123", "org.openpnp.vision.pipeline.stages.MaskCircle", 
                "MaskCircle", "ClassTitle.ID_123", "Mask Circle", "圆形遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_124", "org.openpnp.vision.pipeline.stages.MaskHsv", 
                "MaskHsv", "ClassTitle.ID_124", "Mask HSV", "HSV遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_125", "org.openpnp.vision.pipeline.stages.MaskModel", 
                "MaskModel", "ClassTitle.ID_125", "Mask Model", "模型遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_126", "org.openpnp.vision.pipeline.stages.MaskPolygon", 
                "MaskPolygon", "ClassTitle.ID_126", "Mask Polygon", "多边形遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_127", "org.openpnp.vision.pipeline.stages.MaskRectangle", 
                "MaskRectangle", "ClassTitle.ID_127", "Mask Rectangle", "矩形遮罩", false, "Pipeline"));
        addMapping(new ClassMapping("ID_128", "org.openpnp.vision.pipeline.stages.MatchPartTemplate", 
                "MatchPartTemplate", "ClassTitle.ID_128", "Match Part Template", "匹配元件模板", false, "Pipeline"));
        addMapping(new ClassMapping("ID_129", "org.openpnp.vision.pipeline.stages.MatchPartsTemplate", 
                "MatchPartsTemplate", "ClassTitle.ID_129", "Match Parts Template", "匹配多个元件模板", false, "Pipeline"));
        addMapping(new ClassMapping("ID_130", "org.openpnp.vision.pipeline.stages.MatchTemplate", 
                "MatchTemplate", "ClassTitle.ID_130", "Match Template", "匹配模板", false, "Pipeline"));
        addMapping(new ClassMapping("ID_131", "org.openpnp.vision.pipeline.stages.MinAreaRect", 
                "MinAreaRect", "ClassTitle.ID_131", "Min Area Rect", "最小面积矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_132", "org.openpnp.vision.pipeline.stages.MinAreaRectContours", 
                "MinAreaRectContours", "ClassTitle.ID_132", "Min Area Rect Contours", "轮廓最小面积矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_133", "org.openpnp.vision.pipeline.stages.MinEnclosingCircle", 
                "MinEnclosingCircle", "ClassTitle.ID_133", "Min Enclosing Circle", "最小外接圆", false, "Pipeline"));
        addMapping(new ClassMapping("ID_134", "org.openpnp.vision.pipeline.stages.Normalize", 
                "Normalize", "ClassTitle.ID_134", "Normalize", "归一化", false, "Pipeline"));
        addMapping(new ClassMapping("ID_135", "org.openpnp.vision.pipeline.stages.OrientRotatedRects", 
                "OrientRotatedRects", "ClassTitle.ID_135", "Orient Rotated Rects", "定向旋转矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_136", "org.openpnp.vision.pipeline.stages.ParameterBool", 
                "ParameterBool", "ClassTitle.ID_136", "Parameter Bool", "布尔参数", false, "Pipeline"));
        addMapping(new ClassMapping("ID_137", "org.openpnp.vision.pipeline.stages.ParameterNumeric", 
                "ParameterNumeric", "ClassTitle.ID_137", "Parameter Numeric", "数值参数", false, "Pipeline"));
        addMapping(new ClassMapping("ID_138", "org.openpnp.vision.pipeline.stages.ReadModelProperty", 
                "ReadModelProperty", "ClassTitle.ID_138", "Read Model Property", "读取模型属性", false, "Pipeline"));
        addMapping(new ClassMapping("ID_139", "org.openpnp.vision.pipeline.stages.ReadPartTemplateImage", 
                "ReadPartTemplateImage", "ClassTitle.ID_139", "Read Part Template Image", "读取元件模板图像", false, "Pipeline"));
        addMapping(new ClassMapping("ID_140", "org.openpnp.vision.pipeline.stages.Rotate", 
                "Rotate", "ClassTitle.ID_140", "Rotate", "旋转", false, "Pipeline"));
        addMapping(new ClassMapping("ID_141", "org.openpnp.vision.pipeline.stages.ScriptRun", 
                "ScriptRun", "ClassTitle.ID_141", "Script Run", "运行脚本", false, "Pipeline"));
        addMapping(new ClassMapping("ID_142", "org.openpnp.vision.pipeline.stages.SelectSingleRect", 
                "SelectSingleRect", "ClassTitle.ID_142", "Select Single Rect", "选择单个矩形", false, "Pipeline"));
        addMapping(new ClassMapping("ID_143", "org.openpnp.vision.pipeline.stages.SetColor", 
                "SetColor", "ClassTitle.ID_143", "Set Color", "设置颜色", false, "Pipeline"));
        addMapping(new ClassMapping("ID_144", "org.openpnp.vision.pipeline.stages.SetModel", 
                "SetModel", "ClassTitle.ID_144", "Set Model", "设置模型", false, "Pipeline"));
        addMapping(new ClassMapping("ID_145", "org.openpnp.vision.pipeline.stages.SetResult", 
                "SetResult", "ClassTitle.ID_145", "Set Result", "设置结果", false, "Pipeline"));
        addMapping(new ClassMapping("ID_146", "org.openpnp.vision.pipeline.stages.SimpleBlobDetector", 
                "SimpleBlobDetector", "ClassTitle.ID_146", "Simple Blob Detector", "简单斑点检测", false, "Pipeline"));
        addMapping(new ClassMapping("ID_147", "org.openpnp.vision.pipeline.stages.SimpleOcr", 
                "SimpleOcr", "ClassTitle.ID_147", "Simple OCR", "简单OCR", false, "Pipeline"));
        addMapping(new ClassMapping("ID_148", "org.openpnp.vision.pipeline.stages.SizeCheck", 
                "SizeCheck", "ClassTitle.ID_148", "Size Check", "尺寸检查", false, "Pipeline"));
        addMapping(new ClassMapping("ID_149", "org.openpnp.vision.pipeline.stages.Threshold", 
                "Threshold", "ClassTitle.ID_149", "Threshold", "阈值", false, "Pipeline"));
        addMapping(new ClassMapping("ID_150", "org.openpnp.vision.pipeline.stages.ThresholdAdaptive", 
                "ThresholdAdaptive", "ClassTitle.ID_150", "Adaptive Threshold", "自适应阈值", false, "Pipeline"));
        addMapping(new ClassMapping("ID_151", "org.openpnp.vision.pipeline.stages.WritePartTemplateImage", 
                "WritePartTemplateImage", "ClassTitle.ID_151", "Write Part Template Image", "写入元件模板图像", false, "Pipeline"));
        
        // SPI 基类和接口类
        addMapping(new ClassMapping("ID_152", "org.openpnp.spi.base.AbstractMachine", 
                "AbstractMachine", "ClassTitle.ID_152", "Abstract Machine", "抽象机器", false, "SPI"));
        addMapping(new ClassMapping("ID_153", "org.openpnp.spi.base.AbstractHead", 
                "AbstractHead", "ClassTitle.ID_153", "Abstract Head", "抽象贴装头", true, "SPI"));
        addMapping(new ClassMapping("ID_154", "org.openpnp.spi.base.AbstractNozzle", 
                "AbstractNozzle", "ClassTitle.ID_154", "Abstract Nozzle", "抽象吸嘴", true, "SPI"));
        addMapping(new ClassMapping("ID_155", "org.openpnp.spi.base.AbstractNozzleTip", 
                "AbstractNozzleTip", "ClassTitle.ID_155", "Abstract Nozzle Tip", "抽象吸嘴尖", true, "SPI"));
        addMapping(new ClassMapping("ID_156", "org.openpnp.spi.base.AbstractFeeder", 
                "AbstractFeeder", "ClassTitle.ID_156", "Abstract Feeder", "抽象送料器", true, "SPI"));
        addMapping(new ClassMapping("ID_157", "org.openpnp.spi.base.AbstractCamera", 
                "AbstractCamera", "ClassTitle.ID_157", "Abstract Camera", "抽象相机", true, "SPI"));
        addMapping(new ClassMapping("ID_158", "org.openpnp.spi.base.AbstractActuator", 
                "AbstractActuator", "ClassTitle.ID_158", "Abstract Actuator", "抽象执行器", true, "SPI"));
        addMapping(new ClassMapping("ID_159", "org.openpnp.spi.base.AbstractCoordinateAxis", 
                "AbstractCoordinateAxis", "ClassTitle.ID_159", "Abstract Coordinate Axis", "抽象坐标轴", true, "SPI"));
        addMapping(new ClassMapping("ID_160", "org.openpnp.spi.base.AbstractControllerAxis", 
                "AbstractControllerAxis", "ClassTitle.ID_160", "Abstract Controller Axis", "抽象控制器轴", true, "SPI"));
        addMapping(new ClassMapping("ID_161", "org.openpnp.spi.base.AbstractTransformedAxis", 
                "AbstractTransformedAxis", "ClassTitle.ID_161", "Abstract Transformed Axis", "抽象变换轴", true, "SPI"));
        addMapping(new ClassMapping("ID_162", "org.openpnp.spi.base.AbstractSingleTransformedAxis", 
                "AbstractSingleTransformedAxis", "ClassTitle.ID_162", "Abstract Single Transformed Axis", "抽象单一变换轴", true, "SPI"));
        addMapping(new ClassMapping("ID_163", "org.openpnp.spi.base.AbstractPnpJobProcessor", 
                "AbstractPnpJobProcessor", "ClassTitle.ID_163", "Abstract PnP Job Processor", "抽象贴装作业处理器", false, "SPI"));
        
        // 更多视觉流水线阶段基类
        addMapping(new ClassMapping("ID_164", "org.openpnp.vision.pipeline.CvStage", 
                "CvStage", "ClassTitle.ID_164", "Cv Stage", "视觉流水线阶段", false, "Pipeline"));
        addMapping(new ClassMapping("ID_165", "org.openpnp.vision.pipeline.CvPipeline", 
                "CvPipeline", "ClassTitle.ID_165", "Cv Pipeline", "视觉流水线", false, "Pipeline"));
        addMapping(new ClassMapping("ID_166", "org.openpnp.vision.pipeline.CvAbstractParameterStage", 
                "CvAbstractParameterStage", "ClassTitle.ID_166", "Abstract Parameter Stage", "抽象参数阶段", false, "Pipeline"));
        addMapping(new ClassMapping("ID_167", "org.openpnp.vision.pipeline.CvAbstractScalarParameterStage", 
                "CvAbstractScalarParameterStage", "ClassTitle.ID_167", "Abstract Scalar Parameter Stage", "抽象标量参数阶段", false, "Pipeline"));
        
        // 额外的机器组件
        addMapping(new ClassMapping("ID_168", "org.openpnp.machine.reference.ReferenceMachine", 
                "ReferenceMachine", "ClassTitle.ID_168", "Reference Machine", "参考机器", false, "Machine"));
        addMapping(new ClassMapping("ID_169", "org.openpnp.machine.reference.AbstractReferenceDriver", 
                "AbstractReferenceDriver", "ClassTitle.ID_169", "Abstract Reference Driver", "抽象参考驱动", true, "Driver"));
        
        // 额外的视觉类
        addMapping(new ClassMapping("ID_170", "org.openpnp.machine.reference.vision.AbstractPartSettingsHolder", 
                "AbstractPartSettingsHolder", "ClassTitle.ID_170", "Abstract Part Settings Holder", "抽象元件设置持有者", false, "Vision"));
        addMapping(new ClassMapping("ID_171", "org.openpnp.machine.reference.vision.AbstractPartAlignment", 
                "AbstractPartAlignment", "ClassTitle.ID_171", "Abstract Part Alignment", "抽象元件对齐", false, "Vision"));
        
        // ======== 问题(issues)和解决方案(solutions)文本映射 ========
        // Axis issues & solutions
        addTextMapping(new TextMapping("ID_200", "Axis is not assigned to a driver.", 
                "Axis is not assigned to a driver.", "轴未分配给驱动器。"));
        addTextMapping(new TextMapping("ID_201", "Assign a driver.", 
                "Assign a driver.", "分配一个驱动器。"));
        addTextMapping(new TextMapping("ID_202", "Avoid axis letter E, if possible. Use proper rotation axes instead.", 
                "Avoid axis letter E, if possible. Use proper rotation axes instead.", "尽量避免使用轴字母E，改用合适的旋转轴。"));
        addTextMapping(new TextMapping("ID_203", "Home coordinate is not set.", 
                "Home coordinate is not set.", "未设置原点坐标。"));
        addTextMapping(new TextMapping("ID_204", "Set the home coordinate for the axis.", 
                "Set the home coordinate for the axis.", "设置轴的原点坐标。"));
        addTextMapping(new TextMapping("ID_205", "Axis letter is missing. Assign the letter to continue.", 
                "Axis letter is missing. Assign the letter to continue.", "缺少轴字母，请分配字母以继续。"));
        addTextMapping(new TextMapping("ID_206", "Please assign the correct controller axis letter. Choose from the list or enter a custom letter (some contoller may support an extended range of letters).", 
                "Please assign the correct controller axis letter. Choose from the list or enter a custom letter (some contoller may support an extended range of letters).", "请分配正确的控制器轴字母，从列表中选择或输入自定义字母（某些控制器可能支持扩展字母范围）。"));
        addTextMapping(new TextMapping("ID_207", "Safe Z axis is not configured.", 
                "Safe Z axis is not configured.", "未配置安全Z轴。"));
        addTextMapping(new TextMapping("ID_208", "Configure the Safe Z axis in the axis settings.", 
                "Configure the Safe Z axis in the axis settings.", "在轴设置中配置安全Z轴。"));
        addTextMapping(new TextMapping("ID_209", "Axis letter is not a standard letter. This may cause problems with some controllers.", 
                "Axis letter is not a standard letter. This may cause problems with some controllers.", "轴字母不是标准字母，可能会导致某些控制器出现问题。"));
        addTextMapping(new TextMapping("ID_210", "Transform is not configured for this axis.", 
                "Transform is not configured for this axis.", "未为此轴配置变换。"));
        addTextMapping(new TextMapping("ID_211", "Configure a transform for the axis.", 
                "Configure a transform for the axis.", "为轴配置一个变换。"));
        addTextMapping(new TextMapping("ID_212", "Axis has negative soft limit.", 
                "Axis has negative soft limit.", "轴设置了负软限位。"));
        addTextMapping(new TextMapping("ID_213", "Axis has positive soft limit.", 
                "Axis has positive soft limit.", "轴设置了正软限位。"));
        addTextMapping(new TextMapping("ID_214", "Make sure your home coordinate is within the soft limits you set.", 
                "Make sure your home coordinate is within the soft limits you set.", "确保原点坐标在设置的软限位范围内。"));
        
        // Actuator issues & solutions
        addTextMapping(new TextMapping("ID_215", " is missing a ", 
                " is missing a ", "缺少"));
        addTextMapping(new TextMapping("ID_216", " actuator.", 
                " actuator.", "执行器。"));
        addTextMapping(new TextMapping("ID_217", "Create and assign a ", 
                "Create and assign a ", "创建并分配一个"));
        addTextMapping(new TextMapping("ID_218", " actuator as described in the Wiki.", 
                " actuator as described in the Wiki.", "执行器，如Wiki所述。"));
        addTextMapping(new TextMapping("ID_219", "The ", 
                "The ", ""));
        addTextMapping(new TextMapping("ID_220", " actuator ", 
                " actuator ", "执行器 "));
        addTextMapping(new TextMapping("ID_221", " has no driver assigned.", 
                " has no driver assigned.", "没有分配驱动器。"));
        addTextMapping(new TextMapping("ID_222", "Assign a driver as described in the Wiki.", 
                "Assign a driver as described in the Wiki.", "分配一个驱动器，如Wiki所述。"));
        addTextMapping(new TextMapping("ID_223", "No actuator command configured.", 
                "No actuator command configured.", "未配置执行器命令。"));
        addTextMapping(new TextMapping("ID_224", "Configure the actuator command in the Gcode settings.", 
                "Configure the actuator command in the Gcode settings.", "在Gcode设置中配置执行器命令。"));
        addTextMapping(new TextMapping("ID_225", "Script execuction performance can be improved by enabling engine pooling.", 
                "Script execuction performance can be improved by enabling engine pooling.", "启用引擎池化可提高脚本执行性能。"));
        addTextMapping(new TextMapping("ID_226", "Enable script engine pooling.", 
                "Enable script engine pooling.", "启用脚本引擎池化。"));
        
        // Camera issues & solutions
        addTextMapping(new TextMapping("ID_227", "No camera configured for this machine.", 
                "No camera configured for this machine.", "未为此机器配置相机。"));
        addTextMapping(new TextMapping("ID_228", "Configure a camera in the machine setup.", 
                "Configure a camera in the machine setup.", "在机器设置中配置相机。"));
        addTextMapping(new TextMapping("ID_229", "Camera is not connected.", 
                "Camera is not connected.", "相机未连接。"));
        addTextMapping(new TextMapping("ID_230", "Check the camera connection and settings.", 
                "Check the camera connection and settings.", "检查相机连接和设置。"));
        addTextMapping(new TextMapping("ID_231", "Camera is not settled.", 
                "Camera is not settled.", "相机不稳定。"));
        addTextMapping(new TextMapping("ID_232", "Settling the camera before taking images.", 
                "Settling the camera before taking images.", "在拍摄图像前稳定相机。"));
        addTextMapping(new TextMapping("ID_233", "No upper camera configured.", 
                "No upper camera configured.", "未配置顶部相机。"));
        addTextMapping(new TextMapping("ID_234", "Configure an upper camera for nozzle tip calibration.", 
                "Configure an upper camera for nozzle tip calibration.", "为吸嘴尖校准配置一个顶部相机。"));
        
        // Head issues & solutions
        addTextMapping(new TextMapping("ID_235", "No head configured for this machine.", 
                "No head configured for this machine.", "未为此机器配置贴装头。"));
        addTextMapping(new TextMapping("ID_236", "Configure a head in the machine setup.", 
                "Configure a head in the machine setup.", "在机器设置中配置贴装头。"));
        addTextMapping(new TextMapping("ID_237", "No nozzle configured on head.", 
                "No nozzle configured on head.", "贴装头上未配置吸嘴。"));
        addTextMapping(new TextMapping("ID_238", "Configure one or more nozzles on the head.", 
                "Configure one or more nozzles on the head.", "在贴装头上配置一个或多个吸嘴。"));
        addTextMapping(new TextMapping("ID_239", "Nozzle tip changers are not configured.", 
                "Nozzle tip changers are not configured.", "未配置吸嘴尖更换器。"));
        addTextMapping(new TextMapping("ID_240", "Configure nozzle tip changers on the head.", 
                "Configure nozzle tip changers on the head.", "在贴装头上配置吸嘴尖更换器。"));
        addTextMapping(new TextMapping("ID_241", "No default nozzle tip assigned to nozzle.", 
                "No default nozzle tip assigned to nozzle.", "未为吸嘴分配默认吸嘴尖。"));
        addTextMapping(new TextMapping("ID_242", "Assign a default nozzle tip to the nozzle.", 
                "Assign a default nozzle tip to the nozzle.", "为吸嘴分配一个默认吸嘴尖。"));
        
        // Nozzle Tip issues & solutions
        addTextMapping(new TextMapping("ID_243", "Nozzle tip calibration is required.", 
                "Nozzle tip calibration is required.", "需要校准吸嘴尖。"));
        addTextMapping(new TextMapping("ID_244", "Run the nozzle tip calibration procedure.", 
                "Run the nozzle tip calibration procedure.", "运行吸嘴尖校准程序。"));
        addTextMapping(new TextMapping("ID_245", "Nozzle tip is not compatible with this nozzle.", 
                "Nozzle tip is not compatible with this nozzle.", "吸嘴尖与此吸嘴不兼容。"));
        addTextMapping(new TextMapping("ID_246", "Use a compatible nozzle tip for this nozzle.", 
                "Use a compatible nozzle tip for this nozzle.", "为此吸嘴使用兼容的吸嘴尖。"));
        addTextMapping(new TextMapping("ID_247", "No background calibration method configured.", 
                "No background calibration method configured.", "未配置背景校准方法。"));
        addTextMapping(new TextMapping("ID_248", "Configure a background calibration method.", 
                "Configure a background calibration method.", "配置背景校准方法。"));
        addTextMapping(new TextMapping("ID_249", "No calibration data for this nozzle tip.", 
                "No calibration data for this nozzle tip.", "此吸嘴尖没有校准数据。"));
        addTextMapping(new TextMapping("ID_250", "Calibrate the nozzle tip before use.", 
                "Calibrate the nozzle tip before use.", "使用前校准吸嘴尖。"));
        addTextMapping(new TextMapping("ID_251", "Nozzle tip ID is empty.", 
                "Nozzle tip ID is empty.", "吸嘴尖ID为空。"));
        addTextMapping(new TextMapping("ID_252", "Set a unique ID for the nozzle tip.", 
                "Set a unique ID for the nozzle tip.", "为吸嘴尖设置唯一ID。"));
        
        // Kinematic issues & solutions
        addTextMapping(new TextMapping("ID_253", "To continue, the machine must be enabled and homed.", 
                "To continue, the machine must be enabled and homed.", "要继续，机器必须已启用并回原点。"));
        addTextMapping(new TextMapping("ID_254", "Home the machine now.", 
                "Home the machine now.", "立即回原点。"));
        addTextMapping(new TextMapping("ID_255", "Machine is not enabled.", 
                "Machine is not enabled.", "机器未启用。"));
        addTextMapping(new TextMapping("ID_256", "Enable the machine in the machine controls.", 
                "Enable the machine in the machine controls.", "在机器控制中启用机器。"));
        addTextMapping(new TextMapping("ID_257", "Machine requires homing after enable.", 
                "Machine requires homing after enable.", "启用后需要回原点。"));
        addTextMapping(new TextMapping("ID_258", "Home all axes after enabling the machine.", 
                "Home all axes after enabling the machine.", "启用机器后回所有轴原点。"));
        
        // Gcode Driver issues & solutions
        addTextMapping(new TextMapping("ID_259", "Use the GcodeAsyncDriver for advanced features. Accept or Dismiss to continue.", 
                "Use the GcodeAsyncDriver for advanced features. Accept or Dismiss to continue.", "使用GcodeAsyncDriver获取高级功能。接受或拒绝以继续。"));
        addTextMapping(new TextMapping("ID_260", "Convert to GcodeAsyncDriver.", 
                "Convert to GcodeAsyncDriver.", "转换为GcodeAsyncDriver。"));
        addTextMapping(new TextMapping("ID_261", "Firmware version is , consider upgrading.", 
                "Firmware version is , consider upgrading.", "固件版本未知，考虑升级。"));
        addTextMapping(new TextMapping("ID_262", "Consider upgrading the firmware.", 
                "Consider upgrading the firmware.", "考虑升级固件。"));
        addTextMapping(new TextMapping("ID_263", "No pre-move command configured for this axis.", 
                "No pre-move command configured for this axis.", "未为此轴配置预移动命令。"));
        addTextMapping(new TextMapping("ID_264", "Configure a pre-move command in the Gcode settings.", 
                "Configure a pre-move command in the Gcode settings.", "在Gcode设置中配置预移动命令。"));
        
        // Calibration issues & solutions
        addTextMapping(new TextMapping("ID_265", "Camera calibration is required.", 
                "Camera calibration is required.", "需要校准相机。"));
        addTextMapping(new TextMapping("ID_266", "Run the camera calibration procedure.", 
                "Run the camera calibration procedure.", "运行相机校准程序。"));
        addTextMapping(new TextMapping("ID_267", "Bottom vision calibration is required.", 
                "Bottom vision calibration is required.", "需要校准底部视觉。"));
        addTextMapping(new TextMapping("ID_268", "Run the bottom vision calibration.", 
                "Run the bottom vision calibration.", "运行底部视觉校准。"));
        addTextMapping(new TextMapping("ID_269", "Part alignment calibration is required.", 
                "Part alignment calibration is required.", "需要校准元件对齐。"));
        addTextMapping(new TextMapping("ID_270", "Run the part alignment calibration procedure.", 
                "Run the part alignment calibration procedure.", "运行元件对齐校准程序。"));
        addTextMapping(new TextMapping("ID_271", "Fiducial calibration data not found.", 
                "Fiducial calibration data not found.", "未找到基准点校准数据。"));
        addTextMapping(new TextMapping("ID_272", "Run the fiducial calibration.", 
                "Run the fiducial calibration.", "运行基准点校准。"));
        
        // General / Common
        addTextMapping(new TextMapping("ID_273", "No issues found.", 
                "No issues found.", "未发现问题。"));
        addTextMapping(new TextMapping("ID_274", "Complete the milestone to continue.", 
                "Complete the milestone to continue.", "完成里程碑以继续。"));
        addTextMapping(new TextMapping("ID_275", "Feeder is not configured.", 
                "Feeder is not configured.", "未配置送料器。"));
        addTextMapping(new TextMapping("ID_276", "Configure a feeder for the machine.", 
                "Configure a feeder for the machine.", "为机器配置一个送料器。"));
        addTextMapping(new TextMapping("ID_277", "No nozzle tip configured.", 
                "No nozzle tip configured.", "未配置吸嘴尖。"));
        addTextMapping(new TextMapping("ID_278", "Configure a nozzle tip for the machine.", 
                "Configure a nozzle tip for the machine.", "为机器配置一个吸嘴尖。"));
        addTextMapping(new TextMapping("ID_279", "Driver communication error.", 
                "Driver communication error.", "驱动器通信错误。"));
        addTextMapping(new TextMapping("ID_280", "Check the driver connection and settings.", 
                "Check the driver connection and settings.", "检查驱动器连接和设置。"));
        addTextMapping(new TextMapping("ID_281", "Motion planner is not configured.", 
                "Motion planner is not configured.", "未配置运动规划器。"));
        addTextMapping(new TextMapping("ID_282", "Configure a motion planner for the driver.", 
                "Configure a motion planner for the driver.", "为驱动器配置一个运动规划器。"));
        addTextMapping(new TextMapping("ID_283", "Fiducial locator is not configured.", 
                "Fiducial locator is not configured.", "未配置基准点定位器。"));
        addTextMapping(new TextMapping("ID_284", "Configure the fiducial locator in vision settings.", 
                "Configure the fiducial locator in vision settings.", "在视觉设置中配置基准点定位器。"));
        addTextMapping(new TextMapping("ID_285", "Bottom vision is not configured.", 
                "Bottom vision is not configured.", "未配置底部视觉。"));
        addTextMapping(new TextMapping("ID_286", "Configure bottom vision for part alignment.", 
                "Configure bottom vision for part alignment.", "配置底部视觉用于元件对齐。"));
        addTextMapping(new TextMapping("ID_287", "Issue Description", 
                "Issue Description", "问题简介"));
        addTextMapping(new TextMapping("ID_288", "Solution", 
                "Solution", "解决方案"));
        addTextMapping(new TextMapping("ID_289", "Subject", 
                "Subject", "主题"));
        addTextMapping(new TextMapping("ID_290", "Severity", 
                "Severity", "严重程度"));
        addTextMapping(new TextMapping("ID_291", "State", 
                "State", "状态"));
        
        addTextMapping(new TextMapping("ID_292", "Soft limit warning: axis will stop at the limit.", 
                "Soft limit warning: axis will stop at the limit.", "软限位警告：轴将在限位处停止。"));
        addTextMapping(new TextMapping("ID_293", "Adjust the soft limits to allow full range of motion.", 
                "Adjust the soft limits to allow full range of motion.", "调整软限位以允许完整的运动范围。"));
        addTextMapping(new TextMapping("ID_294", "Driver is not configured.", 
                "Driver is not configured.", "未配置驱动器。"));
        addTextMapping(new TextMapping("ID_295", "Configure a driver for the machine.", 
                "Configure a driver for the machine.", "为机器配置一个驱动器。"));
        addTextMapping(new TextMapping("ID_296", "Axis transform scale factor is zero.", 
                "Axis transform scale factor is zero.", "轴变换比例因子为零。"));
        addTextMapping(new TextMapping("ID_297", "Set the scale factor to a non-zero value.", 
                "Set the scale factor to a non-zero value.", "将比例因子设置为非零值。"));
        
        // ======== 补充缺失的静态文本映射 ========
        // Solutions & Issues 相关的静态文本
        addTextMapping(new TextMapping("ID_300", "Link the Placements/Parts/Packages/Vision Settings/Feeders tables between tabs.", 
                "Link the Placements/Parts/Packages/Vision Settings/Feeders tables between tabs.",
                "链接各标签页之间的贴装/元件/封装/视觉设置/飞达表格。"));
        addTextMapping(new TextMapping("ID_301", 
                "When a table row is selected on one tab, automatically select the corresponding ones on the other tabs. "
                + "For instance, if a placement is selected, the corresponding part will be selected on the Parts tab, "
                + "the package on the Packages tab, the vision settings on the Vision tab, and the feeder on "
                + "the Feeders tab, if one is present for the part.", 
                "When a table row is selected on one tab, automatically select the corresponding ones on the other tabs. "
                + "For instance, if a placement is selected, the corresponding part will be selected on the Parts tab, "
                + "the package on the Packages tab, the vision settings on the Vision tab, and the feeder on "
                + "the Feeders tab, if one is present for the part.",
                "当一个标签页中的表格行被选中时，自动在其他标签页中选择对应的行。例如，如果选中了一个贴装，"
                + "则在元件标签页中选择对应的元件，在封装标签页中选择封装，在视觉设置标签页中选择视觉设置，"
                + "在飞达标签页中选择飞达（如果该元件有对应的飞达）。"));
        addTextMapping(new TextMapping("ID_302", "Enable Visual Homing.", 
                "Enable Visual Homing.", "启用视觉归零。"));
        addTextMapping(new TextMapping("ID_303", 
                "Mount a permanent fiducial to your machine and use it for repeatable precision X/Y homing.", 
                "Mount a permanent fiducial to your machine and use it for repeatable precision X/Y homing.",
                "在机器上安装一个永久基准点，用于可重复的高精度X/Y归零。"));
        addTextMapping(new TextMapping("ID_304", 
                "Feed-rate, acceleration, jerk etc. can now be set individually per axis.", 
                "Feed-rate, acceleration, jerk etc. can now be set individually per axis.",
                "进给率、加速度、加加速度等现在可以单独为每个轴设置。"));
        addTextMapping(new TextMapping("ID_305", 
                "Enable part aligned nozzle rotation mode, so camera view cross-hairs and DRO-coordinates show the "
                + "bottom vision aligned part rotation instead of the unadjusted nozzle rotation.", 
                "Enable part aligned nozzle rotation mode, so camera view cross-hairs and DRO-coordinates show the "
                + "bottom vision aligned part rotation instead of the unadjusted nozzle rotation.",
                "启用与元件对齐的喷嘴旋转模式，使相机视图十字线和DRO坐标显示底部视觉对齐的元件旋转，"
                + "而不是未调整的喷嘴旋转。"));
        addTextMapping(new TextMapping("ID_306", 
                "Rotation can be optimized by wrapping-around the shorter way. Best combined with Limit ±180°.", 
                "Rotation can be optimized by wrapping-around the shorter way. Best combined with Limit ±180°.",
                "旋转可以通过绕最短路径来优化。最好与±180°限制结合使用。"));
        addTextMapping(new TextMapping("ID_307", "Enable Wrap Around.", 
                "Enable Wrap Around.", "启用环绕。"));
        addTextMapping(new TextMapping("ID_308", 
                "A high Preview FPS value might create undue CPU load.", 
                "A high Preview FPS value might create undue CPU load.",
                "高预览FPS值可能会造成过多的CPU负载。"));
        addTextMapping(new TextMapping("ID_309", "Set to 5 FPS.", 
                "Set to 5 FPS.", "设置为5 FPS。"));
        addTextMapping(new TextMapping("ID_310", 
                "It is recommended to suspend camera preview during machine tasks / Jobs.", 
                "It is recommended to suspend camera preview during machine tasks / Jobs.",
                "建议在机器任务/作业期间暂停相机预览。"));
        addTextMapping(new TextMapping("ID_311", "Enable Suspend during tasks.", 
                "Enable Suspend during tasks.", "启用任务期间暂停。"));
        addTextMapping(new TextMapping("ID_312", 
                "In single camera preview OpenPnP can automatically switch the camera for you.", 
                "In single camera preview OpenPnP can automatically switch the camera for you.",
                "在单个相机预览中，OpenPnP可以自动为您切换相机。"));
        addTextMapping(new TextMapping("ID_313", "Enable Auto Camera View.", 
                "Enable Auto Camera View.", "启用自动相机视图。"));
        addTextMapping(new TextMapping("ID_314", "The preview rendering quality can be improved.", 
                "The preview rendering quality can be improved.", "预览渲染质量可以改进。"));
        addTextMapping(new TextMapping("ID_315", 
                "Set to Rendering Quality to High (right click the Camera View to see other options).", 
                "Set to Rendering Quality to High (right click the Camera View to see other options).",
                "将渲染质量设置为高（右键单击相机视图查看其他选项）。"));
        addTextMapping(new TextMapping("ID_316", 
                "For best results with color-keyed computer vision, it is recommended to use static white balance.", 
                "For best results with color-keyed computer vision, it is recommended to use static white balance.",
                "为了获得色彩键控计算机视觉的最佳效果，建议使用静态白平衡。"));
        addTextMapping(new TextMapping("ID_317", 
                "Use an adaptive camera settling method.", 
                "Use an adaptive camera settling method.", "使用自适应相机稳定方法。"));
        addTextMapping(new TextMapping("ID_318", 
                "Set a suitable camera settling method automatically.", 
                "Set a suitable camera settling method automatically.", "自动设置合适的相机稳定方法。"));
        addTextMapping(new TextMapping("ID_319", 
                "To continue, the machine must be enabled and homed.", 
                "To continue, the machine must be enabled and homed.", "要继续，机器必须已启用并回零。"));
        addTextMapping(new TextMapping("ID_320", 
                "Home the machine now.", 
                "Home the machine now.", "立即将机器回零。"));
        addTextMapping(new TextMapping("ID_321", 
                "Invalid Safe Z Zone on", 
                "Invalid Safe Z Zone on", "安全Z区域无效 -"));
        addTextMapping(new TextMapping("ID_322", 
                "Dynamic Safe Z for", 
                "Dynamic Safe Z for", "动态安全Z -"));
        addTextMapping(new TextMapping("ID_323", 
                "The Safe Z Zone is invalid (lower limit > higher limit). Start fresh configuration.", 
                "The Safe Z Zone is invalid (lower limit > higher limit). Start fresh configuration.",
                "安全Z区域无效（下限>上限）。请重新开始配置。"));
        addTextMapping(new TextMapping("ID_324", 
                "has no compatible nozzle.", 
                "has no compatible nozzle.", "没有兼容的喷嘴。"));
        addTextMapping(new TextMapping("ID_325", 
                "Go to the nozzle(s) and enable the Compatible switches where appropriate.", 
                "Go to the nozzle(s) and enable the Compatible switches where appropriate.",
                "前往喷嘴并启用相应的兼容开关。"));
        addTextMapping(new TextMapping("ID_326", 
                "Use the GcodeAsyncDriver for advanced features. Accept or Dismiss to continue.", 
                "Use the GcodeAsyncDriver for advanced features. Accept or Dismiss to continue.",
                "使用GcodeAsyncDriver以使用高级功能。接受或忽略以继续。"));
        addTextMapping(new TextMapping("ID_327", 
                "Convert to GcodeAsyncDriver.", 
                "Convert to GcodeAsyncDriver.", "转换为GcodeAsyncDriver。"));
        addTextMapping(new TextMapping("ID_328", 
                "Connect the driver to your controller.", 
                "Connect the driver to your controller.", "将驱动器连接到您的控制器。"));
        addTextMapping(new TextMapping("ID_329", 
                "Choose the right communications type and port/address settings.", 
                "Choose the right communications type and port/address settings.",
                "选择正确的通信类型和端口/地址设置。"));
        addTextMapping(new TextMapping("ID_330", 
                "has no driver assigned.", 
                "has no driver assigned.", "没有分配驱动器。"));
        addTextMapping(new TextMapping("ID_331", 
                "Assign a driver as described in the Wiki.", 
                "Assign a driver as described in the Wiki.", "按照Wiki所述分配一个驱动器。"));
        addTextMapping(new TextMapping("ID_332", 
                "Pre-rotate bottom vision must be enabled, because the machine has a limited articulation nozzle.", 
                "Pre-rotate bottom vision must be enabled, because the machine has a limited articulation nozzle.",
                "必须启用预旋转底部视觉，因为机器具有有限关节喷嘴。"));
        addTextMapping(new TextMapping("ID_333", 
                "Enable Pre-Rotate.", 
                "Enable Pre-Rotate.", "启用预旋转。"));
        addTextMapping(new TextMapping("ID_334", 
                "Complete milestone", 
                "Complete milestone", "完成里程碑"));
        addTextMapping(new TextMapping("ID_335", 
                "Pre-rotate bottom vision must be allowed on all vision settings, because the machine has a limited articulation nozzle.", 
                "Pre-rotate bottom vision must be allowed on all vision settings, because the machine has a limited articulation nozzle.",
                "必须在所有视觉设置上允许预旋转底部视觉，因为机器具有有限关节喷嘴。"));
        addTextMapping(new TextMapping("ID_336", 
                "has a large Max. Pick Tolerance of", 
                "has a large Max. Pick Tolerance of", "的最大取放公差较大"));
        addTextMapping(new TextMapping("ID_337", 
                "Set the Max. Pick Tolerance to the actual pick errors you expect.", 
                "Set the Max. Pick Tolerance to the actual pick errors you expect.",
                "将最大取放公差设置为您期望的实际取放误差。"));
        addTextMapping(new TextMapping("ID_338", 
                "For a SwitcherCamera it is mandatory to suspend camera preview during machine tasks / Jobs.", 
                "For a SwitcherCamera it is mandatory to suspend camera preview during machine tasks / Jobs.",
                "对于SwitcherCamera，必须暂停机器任务/作业期间的相机预览。"));

        // ======== 模式匹配文本映射（用于包含动态名称的文本）========
        // 每个模式用 (.*?) 捕获动态部分，用 {0}、{1} 等在模板中引用
        addTextMappingPattern(new TextMappingPattern("ID_350",
                "^Set the manual nozzle tip change location for (.*?)\\.$",
                "Set the manual nozzle tip change location for {0}.",
                "设置{0}的手动吸嘴尖更换位置。"));
        addTextMappingPattern(new TextMappingPattern("ID_351",
                "^Jog (.*?) to the manual nozzle tip changing location, then press Accept\\.$",
                "Jog {0} to the manual nozzle tip changing location, then press Accept.",
                "将{0}移动到手动吸嘴尖更换位置，然后按接受。"));
        addTextMappingPattern(new TextMappingPattern("ID_352",
                "^Enable nozzle tip (.*?) calibration\\.$",
                "Enable nozzle tip {0} calibration.",
                "启用吸嘴尖{0}校准。"));
        addTextMappingPattern(new TextMappingPattern("ID_353",
                "^Enable run-out, background and offset calibration for nozzle tip (.*?)\\.$",
                "Enable run-out, background and offset calibration for nozzle tip {0}.",
                "为吸嘴尖{0}启用跳动、背景和偏移校准。"));
        addTextMappingPattern(new TextMappingPattern("ID_354",
                "^Go to Machine Setup / Axes / (\\S+) (\\S+) and tune Feed Rate, Acceleration for best performance\\.$",
                "Go to Machine Setup / Axes / {0} {1} and tune Feed Rate, Acceleration for best performance.",
                "前往机器设置/轴/{0} {1}并调整进给率、加速度以获得最佳性能。"));
        addTextMappingPattern(new TextMappingPattern("ID_355",
                "^Align nozzle (.*?) rotation with part\\.$",
                "Align nozzle {0} rotation with part.",
                "对齐喷嘴{0}与元件的旋转。"));
        addTextMappingPattern(new TextMappingPattern("ID_356",
                "^Calibrate static white balance for camera (.*?)\\.$",
                "Calibrate static white balance for camera {0}.",
                "校准相机{0}的静态白平衡。"));
        addTextMappingPattern(new TextMappingPattern("ID_357",
                "^Calibrate backlash compensation for axis (.*?)\\.$",
                "Calibrate backlash compensation for axis {0}.",
                "校准轴{0}的回差补偿。"));
        addTextMappingPattern(new TextMappingPattern("ID_358",
                "^Dynamic Safe Z for (.*?)\\.$",
                "Dynamic Safe Z for {0}.",
                "{0}的动态安全Z。"));
        addTextMappingPattern(new TextMappingPattern("ID_359",
                "^Nozzle tip (.*?) has no compatible nozzle\\.$",
                "Nozzle tip {0} has no compatible nozzle.",
                "吸嘴尖{0}没有兼容的喷嘴。"));
        addTextMappingPattern(new TextMappingPattern("ID_360",
                "^Set background calibration method for (.*?)\\.$",
                "Set background calibration method for {0}.",
                "设置{0}的背景校准方法。"));
        addTextMappingPattern(new TextMappingPattern("ID_361",
                "^Nozzle (.*?) does not have a Z axis assigned\\.$",
                "Nozzle {0} does not have a Z axis assigned.",
                "喷嘴{0}未分配Z轴。"));
        addTextMappingPattern(new TextMappingPattern("ID_362",
                "^Nozzle (.*?) does not have a Rotation axis assigned\\.$",
                "Nozzle {0} does not have a Rotation axis assigned.",
                "喷嘴{0}未分配旋转轴。"));
        addTextMappingPattern(new TextMappingPattern("ID_363",
                "^For motion control type (.*?) a feed-rate must be set on axis (.*?)\\.$",
                "For motion control type {0} a feed-rate must be set on axis {1}.",
                "对于运动控制类型{0}，必须在轴{1}上设置进给率。"));
        addTextMappingPattern(new TextMappingPattern("ID_364",
                "^For motion control type (.*?) an acceleration limit must be set on axis (.*?)\\.$",
                "For motion control type {0} an acceleration limit must be set on axis {1}.",
                "对于运动控制类型{0}，必须在轴{1}上设置加速度限制。"));
        addTextMappingPattern(new TextMappingPattern("ID_365",
                "^For motion control type (.*?) a jerk limit must be set on axis (.*?)\\.$",
                "For motion control type {0} a jerk limit must be set on axis {1}.",
                "对于运动控制类型{0}，必须在轴{1}上设置加加速度限制。"));
        addTextMappingPattern(new TextMappingPattern("ID_366",
                "^Nozzle tip (.*?) has a large Max\\. Pick Tolerance of (.*?)\\.$",
                "Nozzle tip {0} has a large Max. Pick Tolerance of {1}.",
                "吸嘴尖{0}的最大取放公差{1}较大。"));
        addTextMappingPattern(new TextMappingPattern("ID_367",
                "^Nozzle tip (.*?) has an invalid Min\\. Part Diameter of (.*?)\\.$",
                "Nozzle tip {0} has an invalid Min. Part Diameter of {1}.",
                "吸嘴尖{0}的最小元件直径{1}无效。"));
        addTextMappingPattern(new TextMappingPattern("ID_368",
                "^Nozzle tip (.*?) has a Max\\. Part Diameter that is not larger than the Min\\. Part Diameter\\.$",
                "Nozzle tip {0} has a Max. Part Diameter that is not larger than the Min. Part Diameter.",
                "吸嘴尖{0}的最大元件直径不大于最小元件直径。"));
        addTextMappingPattern(new TextMappingPattern("ID_369",
                "^Rotation axis (.*?) is limiting Nozzle (.*?) to less than 360°\\. "
                + "Must use the (.*?) rotation mode\\.$",
                "Rotation axis {0} is limiting Nozzle {1} to less than 360°. "
                + "Must use the {2} rotation mode.",
                "旋转轴{0}将喷嘴{1}限制在360°以内。必须使用{2}旋转模式。"));
        addTextMappingPattern(new TextMappingPattern("ID_370",
                "^Set the (.*?) rotation mode\\.$",
                "Set the {0} rotation mode.",
                "设置{0}旋转模式。"));
        addTextMappingPattern(new TextMappingPattern("ID_371",
                "^Nozzles (.*?) and (.*?) have the same Z axis assigned\\.$",
                "Nozzles {0} and {1} have the same Z axis assigned.",
                "喷嘴{0}和{1}分配了相同的Z轴。"));
        addTextMappingPattern(new TextMappingPattern("ID_372",
                "^Nozzles (.*?) and (.*?) have the same Rotation axis assigned\\.$",
                "Nozzles {0} and {1} have the same Rotation axis assigned.",
                "喷嘴{0}和{1}分配了相同的旋转轴。"));
        addTextMappingPattern(new TextMappingPattern("ID_373",
                "^Camera (.*?) and (.*?) have the same (.*?) axis (.*?) assigned\\.$",
                "Camera {0} and {1} have the same {2} axis {3} assigned.",
                "相机{0}和{1}分配了相同的{2}轴{3}。"));
        addTextMappingPattern(new TextMappingPattern("ID_374",
                "^Unassign the (.*?) axis (.*?) from the camera (.*?)\\.$",
                "Unassign the {0} axis {1} from the camera {2}.",
                "从相机{2}上取消分配{0}轴{1}。"));
        addTextMappingPattern(new TextMappingPattern("ID_375",
                "^Complete milestone (.*?)$",
                "Complete milestone {0}",
                "完成里程碑：{0}"));
        addTextMappingPattern(new TextMappingPattern("ID_376",
                "^Decide whether (.*?) has dynamic Safe Z or not\\.$",
                "Decide whether {0} has dynamic Safe Z or not.",
                "决定{0}是否具有动态安全Z。"));
        addTextMappingPattern(new TextMappingPattern("ID_377",
                "^Invalid Safe Z Zone on (.*?)\\.$",
                "Invalid Safe Z Zone on {0}.",
                "{0}的安全Z区域无效。"));
        addTextMappingPattern(new TextMappingPattern("ID_378",
                "^The (.*?) actuator (.*?) has no driver assigned\\.$",
                "The {0} actuator {1} has no driver assigned.",
                "{0}执行器{1}未分配驱动器。"));
        addTextMappingPattern(new TextMappingPattern("ID_379",
                "^The (.*?) actuator (.*?) has no (.*?) assigned\\.$",
                "The {0} actuator {1} has no {2} assigned.",
                "{0}执行器{1}未分配{2}。"));
        addTextMappingPattern(new TextMappingPattern("ID_380",
                "^is missing a (.*?) actuator\\.$",
                "is missing a {0} actuator.",
                "缺少{0}执行器。"));
        addTextMappingPattern(new TextMappingPattern("ID_381",
                "^Create and assign a (.*?) actuator as described in the Wiki\\.$",
                "Create and assign a {0} actuator as described in the Wiki.",
                "按照Wiki所述创建并分配一个{0}执行器。"));
        addTextMappingPattern(new TextMappingPattern("ID_382",
                "^For SwitcherCamera it is mandatory to suspend camera preview during machine tasks / Jobs\\.$",
                "For a SwitcherCamera it is mandatory to suspend camera preview during machine tasks / Jobs.",
                "对于SwitcherCamera，必须暂停机器任务/作业期间的相机预览。"));
        addTextMappingPattern(new TextMappingPattern("ID_383",
                "^decide whether (.*?) has dynamic Safe Z or not\\.$",
                "decide whether {0} has dynamic Safe Z or not.",
                "决定{0}是否具有动态安全Z。"));
    }
    
    private static void addMapping(ClassMapping mapping) {
        mappings.add(mapping);
        mappingsByClassName.put(mapping.className, mapping);
        mappingsBySimpleName.put(mapping.simpleName, mapping);
        mappingsById.put(mapping.id, mapping);
    }
    
    private static void addTextMapping(TextMapping mapping) {
        textMappings.add(mapping);
        textMappingsByText.put(mapping.text, mapping);
        textMappingsById.put(mapping.id, mapping);
    }
    
    public static List<ClassMapping> getAllMappings() {
        return new ArrayList<>(mappings);
    }
    
    public static List<TextMapping> getAllTextMappings() {
        return new ArrayList<>(textMappings);
    }
    
    public static ClassMapping getMapping(Class<?> clazz) {
        ClassMapping mapping = mappingsByClassName.get(clazz.getName());
        if (mapping == null) {
            mapping = mappingsBySimpleName.get(clazz.getSimpleName());
        }
        return mapping;
    }
    
    public static ClassMapping getMappingById(String id) {
        return mappingsById.get(id);
    }
    
    public static ClassMapping getMappingByClassName(String className) {
        return mappingsByClassName.get(className);
    }
    
    public static TextMapping getTextMappingByText(String text) {
        return textMappingsByText.get(text);
    }
    
    public static TextMapping getTextMappingById(String id) {
        return textMappingsById.get(id);
    }
    
    public static void saveAll() {
        try {
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取带PID前缀的类名标题
     * 当映射启用时，格式为: (PID:001) Title
     * 当映射未启用时，返回原始类名
     */
    public static String getTitle(Object obj) {
        if (obj == null) {
            return "";
        }
        Class<?> clazz = obj.getClass();
        ClassMapping mapping = getMapping(clazz);
        if (mapping == null) {
            return clazz.getSimpleName();
        }
        // 检查映射启用开关
        if (!isMappingEnabled()) {
            return clazz.getSimpleName();
        }
        String title;
        String currentLanguage = Translations.getLanguage();
        if ("zh_CN".equals(currentLanguage)) {
            title = mapping.chineseTitle;
        } else {
            title = mapping.englishTitle;
        }
        if (mapping.appendName) {
            try {
                String name = (String) clazz.getMethod("getName").invoke(obj);
                if (name != null && !name.isEmpty()) {
                    title += " " + name;
                }
            } catch (Exception ignored) {
            }
        }
        // 添加PID前缀
        return "(PID:" + mapping.id.substring(3) + ") " + title;
    }
    
    /**
     * 获取文本的映射版本，带PID前缀
     * 用于问题/解决方案等文本的映射
     */
    public static String getTextMapping(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        if (!isMappingEnabled()) {
            return text;
        }
        // 1. Try exact text match first
        TextMapping mapping = getTextMappingByText(text);
        if (mapping != null) {
            String currentLanguage = Translations.getLanguage();
            String translatedText;
            if ("zh_CN".equals(currentLanguage)) {
                translatedText = mapping.chineseTranslation;
            } else {
                translatedText = mapping.englishTranslation;
            }
            return "(PID:" + mapping.id.substring(3) + ") " + translatedText;
        }
        // 2. Try pattern matching for dynamic texts (e.g. "Set the manual nozzle tip change location for N1.")
        for (TextMappingPattern patternMapping : textMappingPatterns) {
            java.util.regex.Matcher matcher = patternMapping.pattern.matcher(text);
            if (matcher.matches()) {
                String currentLanguage = Translations.getLanguage();
                String template = "zh_CN".equals(currentLanguage) ? patternMapping.chineseTemplate : patternMapping.englishTemplate;
                String result = template;
                // Replace {0}, {1}, etc. with captured groups
                for (int i = 0; i < matcher.groupCount(); i++) {
                    String varPart = matcher.group(i + 1);
                    result = result.replace("{" + i + "}", varPart);
                }
                return "(PID:" + patternMapping.id.substring(3) + ") " + result;
            }
        }
        return text;
    }
    
    /**
     * 获取所有文本映射模式（供UI显示）
     */
    public static List<TextMappingPattern> getAllTextMappingPatterns() {
        return new ArrayList<>(textMappingPatterns);
    }
    
    private static void addTextMappingPattern(TextMappingPattern pattern) {
        textMappingPatterns.add(pattern);
    }
}