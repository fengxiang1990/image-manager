// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: like.proto

package com.fxa.image.client.proto;

public final class LikeOuter {
  private LikeOuter() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface LikeOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Like)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     *发布者UID
     * </pre>
     *
     * <code>string author = 1;</code>
     * @return The author.
     */
    String getAuthor();
    /**
     * <pre>
     *发布者UID
     * </pre>
     *
     * <code>string author = 1;</code>
     * @return The bytes for author.
     */
    com.google.protobuf.ByteString
        getAuthorBytes();

    /**
     * <code>int64 content_id = 2;</code>
     * @return The contentId.
     */
    long getContentId();

    /**
     * <code>int64 comment_id = 3;</code>
     * @return The commentId.
     */
    long getCommentId();

    /**
     * <pre>
     *true 为点赞 false 为取消点赞
     * </pre>
     *
     * <code>bool like = 4;</code>
     * @return The like.
     */
    boolean getLike();
  }
  /**
   * Protobuf type {@code Like}
   */
  public static final class Like extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:Like)
      LikeOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Like.newBuilder() to construct.
    private Like(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Like() {
      author_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(
        UnusedPrivateParameter unused) {
      return new Like();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Like(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
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
            case 10: {
              String s = input.readStringRequireUtf8();

              author_ = s;
              break;
            }
            case 16: {

              contentId_ = input.readInt64();
              break;
            }
            case 24: {

              commentId_ = input.readInt64();
              break;
            }
            case 32: {

              like_ = input.readBool();
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
      return LikeOuter.internal_static_Like_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LikeOuter.internal_static_Like_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              Like.class, Builder.class);
    }

    public static final int AUTHOR_FIELD_NUMBER = 1;
    private volatile Object author_;
    /**
     * <pre>
     *发布者UID
     * </pre>
     *
     * <code>string author = 1;</code>
     * @return The author.
     */
    @Override
    public String getAuthor() {
      Object ref = author_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        author_ = s;
        return s;
      }
    }
    /**
     * <pre>
     *发布者UID
     * </pre>
     *
     * <code>string author = 1;</code>
     * @return The bytes for author.
     */
    @Override
    public com.google.protobuf.ByteString
        getAuthorBytes() {
      Object ref = author_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        author_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CONTENT_ID_FIELD_NUMBER = 2;
    private long contentId_;
    /**
     * <code>int64 content_id = 2;</code>
     * @return The contentId.
     */
    @Override
    public long getContentId() {
      return contentId_;
    }

    public static final int COMMENT_ID_FIELD_NUMBER = 3;
    private long commentId_;
    /**
     * <code>int64 comment_id = 3;</code>
     * @return The commentId.
     */
    @Override
    public long getCommentId() {
      return commentId_;
    }

    public static final int LIKE_FIELD_NUMBER = 4;
    private boolean like_;
    /**
     * <pre>
     *true 为点赞 false 为取消点赞
     * </pre>
     *
     * <code>bool like = 4;</code>
     * @return The like.
     */
    @Override
    public boolean getLike() {
      return like_;
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getAuthorBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, author_);
      }
      if (contentId_ != 0L) {
        output.writeInt64(2, contentId_);
      }
      if (commentId_ != 0L) {
        output.writeInt64(3, commentId_);
      }
      if (like_ != false) {
        output.writeBool(4, like_);
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getAuthorBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, author_);
      }
      if (contentId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, contentId_);
      }
      if (commentId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, commentId_);
      }
      if (like_ != false) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(4, like_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof Like)) {
        return super.equals(obj);
      }
      Like other = (Like) obj;

      if (!getAuthor()
          .equals(other.getAuthor())) return false;
      if (getContentId()
          != other.getContentId()) return false;
      if (getCommentId()
          != other.getCommentId()) return false;
      if (getLike()
          != other.getLike()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + AUTHOR_FIELD_NUMBER;
      hash = (53 * hash) + getAuthor().hashCode();
      hash = (37 * hash) + CONTENT_ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getContentId());
      hash = (37 * hash) + COMMENT_ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getCommentId());
      hash = (37 * hash) + LIKE_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getLike());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static Like parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Like parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Like parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Like parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Like parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static Like parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static Like parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Like parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static Like parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static Like parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static Like parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static Like parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(Like prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Like}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Like)
        LikeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LikeOuter.internal_static_Like_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LikeOuter.internal_static_Like_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                Like.class, Builder.class);
      }

      // Construct using com.fxa.image.client.proto.publish.LikeOuter.Like.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        author_ = "";

        contentId_ = 0L;

        commentId_ = 0L;

        like_ = false;

        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return LikeOuter.internal_static_Like_descriptor;
      }

      @Override
      public Like getDefaultInstanceForType() {
        return Like.getDefaultInstance();
      }

      @Override
      public Like build() {
        Like result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public Like buildPartial() {
        Like result = new Like(this);
        result.author_ = author_;
        result.contentId_ = contentId_;
        result.commentId_ = commentId_;
        result.like_ = like_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof Like) {
          return mergeFrom((Like)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(Like other) {
        if (other == Like.getDefaultInstance()) return this;
        if (!other.getAuthor().isEmpty()) {
          author_ = other.author_;
          onChanged();
        }
        if (other.getContentId() != 0L) {
          setContentId(other.getContentId());
        }
        if (other.getCommentId() != 0L) {
          setCommentId(other.getCommentId());
        }
        if (other.getLike() != false) {
          setLike(other.getLike());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        Like parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (Like) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private Object author_ = "";
      /**
       * <pre>
       *发布者UID
       * </pre>
       *
       * <code>string author = 1;</code>
       * @return The author.
       */
      public String getAuthor() {
        Object ref = author_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          author_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       *发布者UID
       * </pre>
       *
       * <code>string author = 1;</code>
       * @return The bytes for author.
       */
      public com.google.protobuf.ByteString
          getAuthorBytes() {
        Object ref = author_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          author_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       *发布者UID
       * </pre>
       *
       * <code>string author = 1;</code>
       * @param value The author to set.
       * @return This builder for chaining.
       */
      public Builder setAuthor(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        author_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发布者UID
       * </pre>
       *
       * <code>string author = 1;</code>
       * @return This builder for chaining.
       */
      public Builder clearAuthor() {
        
        author_ = getDefaultInstance().getAuthor();
        onChanged();
        return this;
      }
      /**
       * <pre>
       *发布者UID
       * </pre>
       *
       * <code>string author = 1;</code>
       * @param value The bytes for author to set.
       * @return This builder for chaining.
       */
      public Builder setAuthorBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        author_ = value;
        onChanged();
        return this;
      }

      private long contentId_ ;
      /**
       * <code>int64 content_id = 2;</code>
       * @return The contentId.
       */
      @Override
      public long getContentId() {
        return contentId_;
      }
      /**
       * <code>int64 content_id = 2;</code>
       * @param value The contentId to set.
       * @return This builder for chaining.
       */
      public Builder setContentId(long value) {
        
        contentId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 content_id = 2;</code>
       * @return This builder for chaining.
       */
      public Builder clearContentId() {
        
        contentId_ = 0L;
        onChanged();
        return this;
      }

      private long commentId_ ;
      /**
       * <code>int64 comment_id = 3;</code>
       * @return The commentId.
       */
      @Override
      public long getCommentId() {
        return commentId_;
      }
      /**
       * <code>int64 comment_id = 3;</code>
       * @param value The commentId to set.
       * @return This builder for chaining.
       */
      public Builder setCommentId(long value) {
        
        commentId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 comment_id = 3;</code>
       * @return This builder for chaining.
       */
      public Builder clearCommentId() {
        
        commentId_ = 0L;
        onChanged();
        return this;
      }

      private boolean like_ ;
      /**
       * <pre>
       *true 为点赞 false 为取消点赞
       * </pre>
       *
       * <code>bool like = 4;</code>
       * @return The like.
       */
      @Override
      public boolean getLike() {
        return like_;
      }
      /**
       * <pre>
       *true 为点赞 false 为取消点赞
       * </pre>
       *
       * <code>bool like = 4;</code>
       * @param value The like to set.
       * @return This builder for chaining.
       */
      public Builder setLike(boolean value) {
        
        like_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       *true 为点赞 false 为取消点赞
       * </pre>
       *
       * <code>bool like = 4;</code>
       * @return This builder for chaining.
       */
      public Builder clearLike() {
        
        like_ = false;
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:Like)
    }

    // @@protoc_insertion_point(class_scope:Like)
    private static final Like DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new Like();
    }

    public static Like getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Like>
        PARSER = new com.google.protobuf.AbstractParser<Like>() {
      @Override
      public Like parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Like(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Like> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Like> getParserForType() {
      return PARSER;
    }

    @Override
    public Like getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Like_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Like_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\nlike.proto\"L\n\004Like\022\016\n\006author\030\001 \001(\t\022\022\n\n" +
      "content_id\030\002 \001(\003\022\022\n\ncomment_id\030\003 \001(\003\022\014\n\004" +
      "like\030\004 \001(\010B/\n\"com.fxa.image.client.proto" +
      ".publishB\tLikeOuterb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_Like_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Like_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Like_descriptor,
        new String[] { "Author", "ContentId", "CommentId", "Like", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
