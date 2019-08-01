#include "verifier_VerifySignature.h"
#include <stdio.h>
#include <windows.h>
#include <wincrypt.h>
#include <iostream>
using namespace std;
#pragma comment(lib, "crypt32.lib")

// Функция проверки файла по сертификату
JNIEXPORT jstring JNICALL Java_verifier_VerifySignature_verify
(JNIEnv *env, jobject obj, jbyteArray jxml, jbyteArray jsign){
	jboolean isCopy;
	//Чтение XML-данных
	jbyte* jxml_array = env->GetByteArrayElements(jxml, &isCopy);
	const BYTE* pbContent;
	pbContent = (unsigned char*)jxml_array;
	DWORD cbContent = env->GetArrayLength(jxml);
	//Чтение цифровой подписи
	jbyte* jsign_array = env->GetByteArrayElements(jsign, &isCopy);
	BYTE* pbEncodedBlob;
	pbEncodedBlob = (unsigned char*)jsign_array;
	DWORD cbEncodedBlob = env->GetArrayLength(jsign);

	//Заполняем структуру для верификации
	CRYPT_VERIFY_MESSAGE_PARA msgPara;
	msgPara.cbSize = sizeof(CRYPT_VERIFY_MESSAGE_PARA);
	msgPara.dwMsgAndCertEncodingType =
		X509_ASN_ENCODING | PKCS_7_ASN_ENCODING;
	msgPara.hCryptProv = NULL;
	msgPara.pfnGetSignerCertificate = NULL;
	msgPara.pvGetArg = NULL;

	//Проверка отсоединенной цифровой подписи
	if (CryptVerifyDetachedMessageSignature(
		&msgPara,
		0,
		pbEncodedBlob,
		cbEncodedBlob,
		1,
		&pbContent,
		&cbContent,
		NULL
		))
	{
		return env->NewStringUTF("true");
	} else {
		LPVOID lpMsgBuf;
		FormatMessage(
			FORMAT_MESSAGE_ALLOCATE_BUFFER |
			FORMAT_MESSAGE_FROM_SYSTEM |
			FORMAT_MESSAGE_IGNORE_INSERTS,
			NULL,
			GetLastError(),
			MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // Default language
			(LPTSTR)&lpMsgBuf,
			0,
			NULL
			);
		LPCTSTR strMessage = (LPCTSTR)lpMsgBuf;
		int sizeNeeded = WideCharToMultiByte(CP_UTF8, 0, strMessage, -1, NULL, 0, NULL, NULL);
		char * pszName = new char[sizeNeeded];
		WideCharToMultiByte(CP_UTF8, 0, strMessage, -1, pszName, sizeNeeded, NULL, NULL);
		return env->NewStringUTF(pszName);
	}
}