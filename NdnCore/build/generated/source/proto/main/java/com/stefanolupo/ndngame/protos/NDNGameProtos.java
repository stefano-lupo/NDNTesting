// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NdnGame.proto

package com.stefanolupo.ndngame.protos;

public final class NDNGameProtos {
  private NDNGameProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PlayerStatus_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PlayerStatus_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BulletStatus_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BulletStatus_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BulletsList_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BulletsList_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_BulletRemove_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_BulletRemove_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rNdnGame.proto\"i\n\014PlayerStatus\022\t\n\001x\030\001 \001" +
      "(\005\022\t\n\001y\030\002 \001(\005\022\014\n\004velX\030\003 \001(\005\022\014\n\004velY\030\004 \001(" +
      "\005\022\n\n\002hp\030\005 \001(\005\022\014\n\004mana\030\006 \001(\005\022\r\n\005score\030\007 \001" +
      "(\005\"q\n\014BulletStatus\022\n\n\002id\030\001 \001(\003\022\t\n\001x\030\002 \001(" +
      "\005\022\t\n\001y\030\003 \001(\005\022\014\n\004velX\030\004 \001(\005\022\014\n\004velY\030\005 \001(\005" +
      "\022\016\n\006damage\030\006 \001(\005\022\023\n\013shooterName\030\007 \001(\t\"4\n" +
      "\013BulletsList\022%\n\016bulletStatuses\030\001 \003(\0132\r.B" +
      "ulletStatus\"\032\n\014BulletRemove\022\n\n\002id\030\001 \001(\003B" +
      "1\n\036com.stefanolupo.ndngame.protosB\rNDNGa" +
      "meProtosP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_PlayerStatus_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_PlayerStatus_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PlayerStatus_descriptor,
        new java.lang.String[] { "X", "Y", "VelX", "VelY", "Hp", "Mana", "Score", });
    internal_static_BulletStatus_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_BulletStatus_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BulletStatus_descriptor,
        new java.lang.String[] { "Id", "X", "Y", "VelX", "VelY", "Damage", "ShooterName", });
    internal_static_BulletsList_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_BulletsList_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BulletsList_descriptor,
        new java.lang.String[] { "BulletStatuses", });
    internal_static_BulletRemove_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_BulletRemove_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_BulletRemove_descriptor,
        new java.lang.String[] { "Id", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
