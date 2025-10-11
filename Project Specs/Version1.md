# TurboEdit Project File Specification 1

The file is serialized by [MessagePack](https://msgpack.org/) as an array.

The file extension will be `.tvp` (**T**urbo**V**ideo **P**roject).

`int64` = `long` (**NOT** BigInteger)

## Project

| Name        | Type                            | Notes / Description                                  |
|-------------|---------------------------------|------------------------------------------------------|
| FileVersion | int32                           | The file version of the project file (0 for spec. 1) |
| Name        | string                          | The project name                                     |
| Files       | [ProjectFile](#projectfile)[]   | The imported files for the project                   |
| Timelines   | [TimelineData](#timelinedata)[] | Project Timelines                                    |

## ProjectFile

| Name         | Type                          | Notes / Description                                       |
|--------------|-------------------------------|-----------------------------------------------------------|
| Name         | string                        | The name of the file                                      |
| Path         | string                        | The relative path (from the **Project File**) of the file |
| Type         | string                        | The MIME-Type of the file                                 |
| PreviewImage | byte[]                        | Preview image (First frame of video) in JPEG format       |
| IsVideo      | bool                          |                                                           |
| VideoHeight  | int32                         | Only present if `IsVideo` is `true`                       |
| VideoWidth   | int32                         | Only present if `IsVideo` is `true`                       |
| VideoFPS     | int32                         | Only present if `IsVideo` is `true`                       |
| Audio        | [AudioObject](#audioobject)[] | Only present if `IsVideo` is `true`                       |

## AudioObject

| Name      | Type   | Notes / Description                    |
|-----------|--------|----------------------------------------|
| Layer     | int32  | Layer ID                               |
| LayerName | string | Name of the audio layer                |
| Waveform  | byte[] | Audio waveform in JPEG format          |

## TimelineData

| Name        | Type                              | Notes / Description                |
|-------------|-----------------------------------|------------------------------------|
| Name        | string                            | Timeline name                      |
| Length      | int64                             | Length of timeline in milliseconds |
| VideoLayers | [TimelineLayer](#timelinelayer)[] | Video Layers of the timeline       |
| AudioLayers | [TimelineLayer](#timelinelayer)[] | Audio Layers of the timeline       |
| Clips       | [TimelineCip](#timelineclip)[]    | Clips on the timeline              |

## TimelineLayer

| Name    | Type   | Notes / Description            |
|---------|--------|--------------------------------|
| Name    | string | Name of the layer              |
| Enabled | bool   | If the layer is enabled or not |

## TimelineClip

| Name      | Type  | Notes / Description                              |
|-----------|-------|--------------------------------------------------|
| File      | int32 | Index of [ProjectFile](#projectfile) of the clip |
| Position  | int64 | Position of clip                                 |
| StartTime | int64 | Clip-Relative Start time in milliseconds         |
| EndTime   | int64 | Clip-Relative End time in milliseconds           |

---

Created by **[@Turboman3000](https://github.com/Turboman3000)**

Document Locked: **- - - - / - - / - -**
