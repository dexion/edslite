package com.sovworks.eds.crypto.blockciphers;

import com.sovworks.eds.crypto.EncryptionEngineException;
import com.sovworks.eds.crypto.BlockCipherNative;


public class GOST implements BlockCipherNative
{
    @Override
    public void init(byte[] key) throws EncryptionEngineException
    {
        _contextPtr = initContext(key);
        if(_contextPtr == 0)
            throw new EncryptionEngineException("GOST context initialization failed");

    }
    @Override
    public void encryptBlock(byte[] data) throws EncryptionEngineException
    {
        if(_contextPtr==0)
            throw new EncryptionEngineException("Cipher is closed");
        encrypt(data,_contextPtr);

    }
    @Override
    public void decryptBlock(byte[] data) throws EncryptionEngineException
    {
        if(_contextPtr==0)
            throw new EncryptionEngineException("Cipher is closed");
        decrypt(data,_contextPtr);
    }

    @Override
    public void close()
    {
        if(_contextPtr!=0)
        {
            closeContext(_contextPtr);
            _contextPtr = 0;
        }
    }

    @Override
    public long getNativeInterfacePointer() throws EncryptionEngineException
    {
        return _contextPtr;
    }

    @Override
    public int getKeySize()
    {
        return 32;
    }

    @Override
    public int getBlockSize()
    {
        return 8;
    }

    public void useTestSubstMask(boolean val)
    {
        _useTestSubstMask = val;
    }

    static
    {
        System.loadLibrary("edsgost");
    }

    private long _contextPtr;
    private boolean _useTestSubstMask;

    private native long initContext(byte[] key);
    private native void closeContext(long contextPtr);
    private native void encrypt(byte[] data,long contextPtr);
    private native void decrypt(byte[] data,long contextPtr);
}

