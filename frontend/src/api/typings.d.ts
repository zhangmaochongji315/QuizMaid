declare namespace API {
  type ApplicationContext = {
    parent?: ApplicationContext
    id?: string
    displayName?: string
    applicationName?: string
    startupDate?: number
    autowireCapableBeanFactory?: AutowireCapableBeanFactory
    environment?: Environment
    beanDefinitionNames?: string[]
    beanDefinitionCount?: number
    parentBeanFactory?: BeanFactory
    classLoader?: {
      name?: string
      registeredAsParallelCapable?: boolean
      parent?: {
        name?: string
        registeredAsParallelCapable?: boolean
        unnamedModule?: {
          name?: string
          classLoader?: {
            name?: string
            registeredAsParallelCapable?: boolean
            definedPackages?: {
              name?: string
              annotations?: Record<string, any>[]
              declaredAnnotations?: Record<string, any>[]
              sealed?: boolean
              specificationTitle?: string
              specificationVersion?: string
              specificationVendor?: string
              implementationTitle?: string
              implementationVersion?: string
              implementationVendor?: string
            }[]
            defaultAssertionStatus?: boolean
          }
          descriptor?: { open?: boolean; automatic?: boolean }
          named?: boolean
          annotations?: Record<string, any>[]
          declaredAnnotations?: Record<string, any>[]
          packages?: string[]
          nativeAccessEnabled?: boolean
          layer?: Record<string, any>
        }
        definedPackages?: {
          name?: string
          annotations?: Record<string, any>[]
          declaredAnnotations?: Record<string, any>[]
          sealed?: boolean
          specificationTitle?: string
          specificationVersion?: string
          specificationVendor?: string
          implementationTitle?: string
          implementationVersion?: string
          implementationVendor?: string
        }[]
        defaultAssertionStatus?: boolean
      }
      unnamedModule?: {
        name?: string
        classLoader?: {
          name?: string
          registeredAsParallelCapable?: boolean
          definedPackages?: {
            name?: string
            annotations?: Record<string, any>[]
            declaredAnnotations?: Record<string, any>[]
            sealed?: boolean
            specificationTitle?: string
            specificationVersion?: string
            specificationVendor?: string
            implementationTitle?: string
            implementationVersion?: string
            implementationVendor?: string
          }[]
          defaultAssertionStatus?: boolean
        }
        descriptor?: { open?: boolean; automatic?: boolean }
        named?: boolean
        annotations?: Record<string, any>[]
        declaredAnnotations?: Record<string, any>[]
        packages?: string[]
        nativeAccessEnabled?: boolean
        layer?: Record<string, any>
      }
      definedPackages?: {
        name?: string
        annotations?: Record<string, any>[]
        declaredAnnotations?: Record<string, any>[]
        sealed?: boolean
        specificationTitle?: string
        specificationVersion?: string
        specificationVendor?: string
        implementationTitle?: string
        implementationVersion?: string
        implementationVendor?: string
      }[]
      defaultAssertionStatus?: boolean
    }
  }

  type AutowireCapableBeanFactory = true

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseInteger = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseListUserHeatMapVO = {
    code?: number
    data?: UserHeatMapVO[]
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseMapLocalDateBoolean = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponsePageUserVO = {
    code?: number
    data?: PageUserVO
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUser = {
    code?: number
    data?: User
    message?: string
  }

  type BaseResponseUserLoginVO = {
    code?: number
    data?: UserLoginVO
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type BeanFactory = true

  type BindEmailDTO = {
    email?: string
    code?: string
  }

  type callbackParams = {
    code: string
  }

  type DeleteRequest = {
    id?: number
  }

  type Environment = {
    defaultProfiles?: string[]
    activeProfiles?: string[]
  }

  type FilterRegistration = {
    urlPatternMappings?: string[]
    servletNameMappings?: string[]
    initParameters?: Record<string, any>
    name?: string
    className?: string
  }

  type getSignInDaysParams = {
    userId: number
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserSignDataParams = {
    year: number
  }

  type getUserVoByIdParams = {
    id: number
  }

  type HttpStatusCode = {
    is2xxSuccessful?: boolean
    is4xxClientError?: boolean
    is5xxServerError?: boolean
    is1xxInformational?: boolean
    is3xxRedirection?: boolean
    error?: boolean
  }

  type JspConfigDescriptor = {
    taglibs?: TaglibDescriptor[]
    jspPropertyGroups?: JspPropertyGroupDescriptor[]
  }

  type JspPropertyGroupDescriptor = {
    defaultContentType?: string
    urlPatterns?: string[]
    deferredSyntaxAllowedAsLiteral?: string
    trimDirectiveWhitespaces?: string
    errorOnUndeclaredNamespace?: string
    isXml?: string
    errorOnELNotFound?: string
    scriptingInvalid?: string
    elIgnored?: string
    includePreludes?: string[]
    pageEncoding?: string
    includeCodas?: string[]
    buffer?: string
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type RedirectView = {
    applicationContext?: ApplicationContext
    servletContext?: ServletContext
    contentType?: string
    requestContextAttribute?: string
    staticAttributes?: Record<string, any>
    exposePathVariables?: boolean
    exposeContextBeansAsAttributes?: boolean
    exposedContextBeanNames?: string[]
    beanName?: string
    url?: string
    contextRelative?: boolean
    http10Compatible?: boolean
    exposeModelAttributes?: boolean
    encodingScheme?: string
    statusCode?: HttpStatusCode
    expandUriTemplateVariables?: boolean
    propagateQueryParams?: boolean
    hosts?: string[]
    redirectView?: boolean
    propagateQueryProperties?: boolean
    attributesMap?: Record<string, any>
    attributesCSV?: string
    attributes?: Record<string, any>
  }

  type ResetPasswordDTO = {
    password?: string
    email?: string
    code?: string
  }

  type sendEmailParams = {
    email: string
  }

  type ServletContext = {
    initParameterNames?: Record<string, any>
    contextPath?: string
    virtualServerName?: string
    sessionCookieConfig?: SessionCookieConfig
    sessionTimeout?: number
    serverInfo?: string
    defaultSessionTrackingModes?: ('COOKIE' | 'URL' | 'SSL')[]
    effectiveSessionTrackingModes?: ('COOKIE' | 'URL' | 'SSL')[]
    requestCharacterEncoding?: string
    responseCharacterEncoding?: string
    sessionTrackingModes?: ('COOKIE' | 'URL' | 'SSL')[]
    effectiveMinorVersion?: number
    servletRegistrations?: Record<string, any>
    filterRegistrations?: Record<string, any>
    jspConfigDescriptor?: JspConfigDescriptor
    servletContextName?: string
    effectiveMajorVersion?: number
    classLoader?: {
      name?: string
      registeredAsParallelCapable?: boolean
      definedPackages?: {
        name?: string
        annotations?: Record<string, any>[]
        declaredAnnotations?: Record<string, any>[]
        sealed?: boolean
        specificationTitle?: string
        specificationVersion?: string
        specificationVendor?: string
        implementationTitle?: string
        implementationVersion?: string
        implementationVendor?: string
      }[]
      defaultAssertionStatus?: boolean
    }
    majorVersion?: number
    minorVersion?: number
    attributeNames?: Record<string, any>
  }

  type ServletRegistration = {
    mappings?: string[]
    runAsRole?: string
    initParameters?: Record<string, any>
    name?: string
    className?: string
  }

  type SessionCookieConfig = {
    secure?: boolean
    httpOnly?: boolean
    domain?: string
    path?: string
    maxAge?: number
    name?: string
    attributes?: Record<string, any>
    comment?: string
  }

  type TaglibDescriptor = {
    taglibURI?: string
    taglibLocation?: string
  }

  type User = {
    id?: number
    username?: string
    password?: string
    nickname?: string
    email?: string
    role?: string
    emailVerified?: number
    oauthType?: string
    oauthOpenid?: string
    status?: number
    answerNum?: number
    correctNum?: number
    createTime?: string
    updateTime?: string
    isDeleted?: number
  }

  type UserAddDTO = {
    username?: string
    nickname?: string
    role?: string
  }

  type UserHeatMapVO = {
    date?: string
    count?: number
    level?: number
  }

  type UserLoginByEmailDTO = {
    email?: string
    code?: string
  }

  type UserLoginDTO = {
    username?: string
    userPassword?: string
  }

  type UserLoginVO = {
    id?: number
    username?: string
    password?: string
    nickname?: string
    email?: string
    role?: string
    emailVerified?: number
    oauthType?: string
    oauthOpenid?: string
    status?: number
    answerNum?: number
    correctNum?: number
    createTime?: string
    updateTime?: string
  }

  type UserQueryDTO = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    nickname?: string
    username?: string
    role?: string
  }

  type UserRegisterByEmailDTO = {
    userName?: string
    userPassword?: string
    checkUserPassword?: string
    email?: string
    code?: string
  }

  type UserRegisterDTO = {
    userName?: string
    userPassword?: string
    checkUserPassword?: string
  }

  type UserUpdateByAdminDTO = {
    id?: number
    username?: string
    nickname?: string
    email?: string
    role?: string
    emailVerified?: number
    status?: number
    answerNum?: number
    correctNum?: number
  }

  type UserUpdateDTO = {
    id?: number
    nickname?: string
    email?: string
  }

  type UserVO = {
    id?: number
    username?: string
    nickname?: string
    email?: string
    role?: string
    emailVerified?: number
    oauthType?: string
    oauthOpenid?: string
    status?: number
    answerNum?: number
    correctNum?: number
    createTime?: string
    updateTime?: string
  }
}
