package com.zql.filepickerlib.mimeType;


public class MimeTypeInfo {
    public MimeTypeCategory mCategory;
    public String mMimeType;
    public String mDrawable;

    MimeTypeInfo() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.mCategory == null) ? 0 : this.mCategory.hashCode());
        result = prime * result
                + ((this.mDrawable == null) ? 0 : this.mDrawable.hashCode());
        result = prime * result
                + ((this.mMimeType == null) ? 0 : this.mMimeType.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MimeTypeInfo other = (MimeTypeInfo) obj;
        if (this.mCategory != other.mCategory)
            return false;
        if (this.mDrawable == null) {
            if (other.mDrawable != null)
                return false;
        } else if (!this.mDrawable.equals(other.mDrawable))
            return false;
        if (this.mMimeType == null) {
            return other.mMimeType == null;
        } else return this.mMimeType.equals(other.mMimeType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MimeTypeInfo [mCategory=" + this.mCategory +
                ", mMimeType=" + this.mMimeType +
                ", mDrawable=" + this.mDrawable + "]";
    }

}
