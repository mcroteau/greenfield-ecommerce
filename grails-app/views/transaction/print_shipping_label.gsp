<g:if test="${transactionInstance?.postageUrl}">
	<img src="${transactionInstance.postageUrl}" >
</g:if>
<g:else>
	<h2>Something went wrong while processing request</h2>
</g:else>