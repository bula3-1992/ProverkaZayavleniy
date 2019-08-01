#include "stdafx.h"
#include <stdio.h>
#include <windows.h>
#include <iostream>

using namespace std;
typedef HRESULT(__cdecl *CPSNINITLIB2)(HWND, char *, char *);
typedef HRESULT(__cdecl *CPSNVALIDATE)(char *, UINT *, UINT *);

int _tmain(int argc, _TCHAR* argv[])
{
	if (argc == 4){
		int length = WideCharToMultiByte(CP_UTF8, 0, argv[1], -1, 0, 0, NULL, NULL);
		char* libPath = new char[length];
		WideCharToMultiByte(CP_UTF8, 0, argv[1], -1, libPath, length, NULL, NULL);
		char* libFile = new char[length + 13];
		strcpy_s(libFile, length, libPath);
		const char* suffix = "\\cpsnxml.dll";
		strcat_s(libFile, length + 12, suffix);

		length = WideCharToMultiByte(CP_UTF8, 0, argv[2], -1, 0, 0, NULL, NULL);
		char* kladrPath = new char[length];
		WideCharToMultiByte(CP_UTF8, 0, argv[2], -1, kladrPath, length, NULL, NULL);

		length = WideCharToMultiByte(CP_UTF8, 0, argv[3], -1, 0, 0, NULL, NULL);
		char* fileName = new char[length];
		WideCharToMultiByte(CP_UTF8, 0, argv[3], -1, fileName, length, NULL, NULL);
		int result = 1;//Ошибка

		// Get a handle to the DLL module.
		HINSTANCE xmlLibHandle = LoadLibraryA(libFile);
		CPSNINITLIB2 cpsnInitLib2;
		CPSNVALIDATE cpsnValidate;
		BOOL fFreeResult, fRunTimeLinkSuccess = FALSE;

		// If the handle is valid, try to get the function address.
		if (xmlLibHandle != NULL){
			cpsnInitLib2 = (CPSNINITLIB2)GetProcAddress(xmlLibHandle, "cpsnInitLib2");
			try{
				// If the function address is valid, call the function.	
				if (NULL != cpsnInitLib2){
					HRESULT hres = cpsnInitLib2(0, libPath, kladrPath);
					//0 – в случае успеха
					if (hres == 0){
						// If the cpsnInitLib function is ok, call the function.
						cpsnValidate = (CPSNVALIDATE)GetProcAddress(xmlLibHandle, "cpsnValidate");
						if (fRunTimeLinkSuccess = (NULL != cpsnValidate)){
							UINT pnError, pnWarning;
							HRESULT hres = cpsnValidate(fileName, &pnError, &pnWarning);
							result = hres;
						}
					}
				}
			}
			catch (int){
			}
			// Free the DLL module.
			fFreeResult = FreeLibrary(xmlLibHandle);
		}

		// If unable to call the DLL function, use an alternative.
		if (!fRunTimeLinkSuccess)
			printf("Failes to load library\n");
		return result;
	}
	else{
		setlocale(LC_ALL, "RUSSIAN");
		printf("Программа для проверки CheckXML - ОПФР по РБ\n");
		printf("Параметры запуска\n");
		printf("1: Папка с CheckXML\n");
		printf("2: Папка с базой адресов КЛАДР\n");
		printf("3: Путь к XML файлу\n");
		printf("Результат, возвращаемое значение\n");
		printf("-N - Ошибка\n");
		printf("0 - Успешно\n");
		return -1;
	}
}


