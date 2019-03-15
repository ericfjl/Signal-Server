// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PubSubMessage.proto

package org.whispersystems.textsecuregcm.storage;

public final class PubSubProtos {
  private PubSubProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface PubSubMessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:textsecure.PubSubMessage)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
     */
    boolean hasType();
    /**
     * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
     */
    org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type getType();

    /**
     * <code>optional bytes content = 2;</code>
     */
    boolean hasContent();
    /**
     * <code>optional bytes content = 2;</code>
     */
    com.google.protobuf.ByteString getContent();
  }
  /**
   * Protobuf type {@code textsecure.PubSubMessage}
   */
  public  static final class PubSubMessage extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:textsecure.PubSubMessage)
      PubSubMessageOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use PubSubMessage.newBuilder() to construct.
    private PubSubMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private PubSubMessage() {
      type_ = 0;
      content_ = com.google.protobuf.ByteString.EMPTY;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private PubSubMessage(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              int rawValue = input.readEnum();
                @SuppressWarnings("deprecation")
              org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type value = org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = rawValue;
              }
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              content_ = input.readBytes();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.whispersystems.textsecuregcm.storage.PubSubProtos.internal_static_textsecure_PubSubMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.whispersystems.textsecuregcm.storage.PubSubProtos.internal_static_textsecure_PubSubMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.class, org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Builder.class);
    }

    /**
     * Protobuf enum {@code textsecure.PubSubMessage.Type}
     */
    public enum Type
        implements com.google.protobuf.ProtocolMessageEnum {
      /**
       * <code>UNKNOWN = 0;</code>
       */
      UNKNOWN(0),
      /**
       * <code>QUERY_DB = 1;</code>
       */
      QUERY_DB(1),
      /**
       * <code>DELIVER = 2;</code>
       */
      DELIVER(2),
      /**
       * <code>KEEPALIVE = 3;</code>
       */
      KEEPALIVE(3),
      /**
       * <code>CLOSE = 4;</code>
       */
      CLOSE(4),
      /**
       * <code>CONNECTED = 5;</code>
       */
      CONNECTED(5),
      ;

      /**
       * <code>UNKNOWN = 0;</code>
       */
      public static final int UNKNOWN_VALUE = 0;
      /**
       * <code>QUERY_DB = 1;</code>
       */
      public static final int QUERY_DB_VALUE = 1;
      /**
       * <code>DELIVER = 2;</code>
       */
      public static final int DELIVER_VALUE = 2;
      /**
       * <code>KEEPALIVE = 3;</code>
       */
      public static final int KEEPALIVE_VALUE = 3;
      /**
       * <code>CLOSE = 4;</code>
       */
      public static final int CLOSE_VALUE = 4;
      /**
       * <code>CONNECTED = 5;</code>
       */
      public static final int CONNECTED_VALUE = 5;


      public final int getNumber() {
        return value;
      }

      /**
       * @deprecated Use {@link #forNumber(int)} instead.
       */
      @java.lang.Deprecated
      public static Type valueOf(int value) {
        return forNumber(value);
      }

      public static Type forNumber(int value) {
        switch (value) {
          case 0: return UNKNOWN;
          case 1: return QUERY_DB;
          case 2: return DELIVER;
          case 3: return KEEPALIVE;
          case 4: return CLOSE;
          case 5: return CONNECTED;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static final com.google.protobuf.Internal.EnumLiteMap<
          Type> internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Type>() {
              public Type findValueByNumber(int number) {
                return Type.forNumber(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(ordinal());
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.getDescriptor().getEnumTypes().get(0);
      }

      private static final Type[] VALUES = values();

      public static Type valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int value;

      private Type(int value) {
        this.value = value;
      }

      // @@protoc_insertion_point(enum_scope:textsecure.PubSubMessage.Type)
    }

    private int bitField0_;
    public static final int TYPE_FIELD_NUMBER = 1;
    private int type_;
    /**
     * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
     */
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
     */
    public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type getType() {
      @SuppressWarnings("deprecation")
      org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type result = org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type.valueOf(type_);
      return result == null ? org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type.UNKNOWN : result;
    }

    public static final int CONTENT_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString content_;
    /**
     * <code>optional bytes content = 2;</code>
     */
    public boolean hasContent() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes content = 2;</code>
     */
    public com.google.protobuf.ByteString getContent() {
      return content_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, type_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, content_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, type_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, content_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage)) {
        return super.equals(obj);
      }
      org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage other = (org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage) obj;

      boolean result = true;
      result = result && (hasType() == other.hasType());
      if (hasType()) {
        result = result && type_ == other.type_;
      }
      result = result && (hasContent() == other.hasContent());
      if (hasContent()) {
        result = result && getContent()
            .equals(other.getContent());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasType()) {
        hash = (37 * hash) + TYPE_FIELD_NUMBER;
        hash = (53 * hash) + type_;
      }
      if (hasContent()) {
        hash = (37 * hash) + CONTENT_FIELD_NUMBER;
        hash = (53 * hash) + getContent().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code textsecure.PubSubMessage}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:textsecure.PubSubMessage)
        org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.whispersystems.textsecuregcm.storage.PubSubProtos.internal_static_textsecure_PubSubMessage_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.whispersystems.textsecuregcm.storage.PubSubProtos.internal_static_textsecure_PubSubMessage_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.class, org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Builder.class);
      }

      // Construct using org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        type_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        content_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.whispersystems.textsecuregcm.storage.PubSubProtos.internal_static_textsecure_PubSubMessage_descriptor;
      }

      @java.lang.Override
      public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage getDefaultInstanceForType() {
        return org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.getDefaultInstance();
      }

      @java.lang.Override
      public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage build() {
        org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage buildPartial() {
        org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage result = new org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.content_ = content_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage) {
          return mergeFrom((org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage other) {
        if (other == org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasContent()) {
          setContent(other.getContent());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int type_ = 0;
      /**
       * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
       */
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
       */
      public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type getType() {
        @SuppressWarnings("deprecation")
        org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type result = org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type.valueOf(type_);
        return result == null ? org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type.UNKNOWN : result;
      }
      /**
       * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
       */
      public Builder setType(org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage.Type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        type_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>optional .textsecure.PubSubMessage.Type type = 1;</code>
       */
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString content_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes content = 2;</code>
       */
      public boolean hasContent() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes content = 2;</code>
       */
      public com.google.protobuf.ByteString getContent() {
        return content_;
      }
      /**
       * <code>optional bytes content = 2;</code>
       */
      public Builder setContent(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        content_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes content = 2;</code>
       */
      public Builder clearContent() {
        bitField0_ = (bitField0_ & ~0x00000002);
        content_ = getDefaultInstance().getContent();
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:textsecure.PubSubMessage)
    }

    // @@protoc_insertion_point(class_scope:textsecure.PubSubMessage)
    private static final org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage();
    }

    public static org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<PubSubMessage>
        PARSER = new com.google.protobuf.AbstractParser<PubSubMessage>() {
      @java.lang.Override
      public PubSubMessage parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new PubSubMessage(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<PubSubMessage> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<PubSubMessage> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public org.whispersystems.textsecuregcm.storage.PubSubProtos.PubSubMessage getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_textsecure_PubSubMessage_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_textsecure_PubSubMessage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023PubSubMessage.proto\022\ntextsecure\"\247\001\n\rPu" +
      "bSubMessage\022,\n\004type\030\001 \001(\0162\036.textsecure.P" +
      "ubSubMessage.Type\022\017\n\007content\030\002 \001(\014\"W\n\004Ty" +
      "pe\022\013\n\007UNKNOWN\020\000\022\014\n\010QUERY_DB\020\001\022\013\n\007DELIVER" +
      "\020\002\022\r\n\tKEEPALIVE\020\003\022\t\n\005CLOSE\020\004\022\r\n\tCONNECTE" +
      "D\020\005B8\n(org.whispersystems.textsecuregcm." +
      "storageB\014PubSubProtos"
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
    internal_static_textsecure_PubSubMessage_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_textsecure_PubSubMessage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_textsecure_PubSubMessage_descriptor,
        new java.lang.String[] { "Type", "Content", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
